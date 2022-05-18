package com.oxo.ball.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallGameLossPerCent;
import com.oxo.ball.bean.dao.BallMenu;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.api.*;
import com.oxo.ball.contant.RedisKeyContant;
import com.oxo.ball.service.admin.BallMenuService;
import com.oxo.ball.service.admin.IBallBetService;
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import com.oxo.ball.service.admin.IBallGameService;
import com.oxo.ball.service.player.IPlayerBetService;
import com.oxo.ball.utils.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@RestController()
@RequestMapping("test")
public class TestController {

    @Resource
    BallMenuService ballMenuService;

    @Resource
    IPlayerBetService betService;
    @Autowired
    IBallBetService ballBetService;

    @Autowired
    IBallGameLossPerCentService lossPerCentService;

    @Autowired
    IBallGameService gameService;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("orderno")
    public Object orderNo(){
        return betService.getDayOrderNo();
    }
    @GetMapping("menu")
    public Object testMenu(){
        List<BallMenu> all = ballMenuService.findAll();
        List<Menu> tree = new ArrayList<>();
        for(BallMenu ballMenu:all){
            //一级菜单放入
            if(ballMenu.getParentId()==0){
                tree.add(Menu.builder()
                        .id(ballMenu.getId())
                        .label(ballMenu.getMenuName())
                        .children(new ArrayList<>())
                        .sort(ballMenu.getIsMenu())
                        .build());
            }
        }
        for(BallMenu ballMenu:all){
            if(ballMenu.getParentId()==0){
                continue;
            }
            for(Menu menu:tree){
                if(menu.getId().equals(ballMenu.getParentId())){
                    //放入二级
                    List<Menu> children = menu.getChildren();
                    children.add(Menu.builder()
                            .id(ballMenu.getId())
                            .label(ballMenu.getMenuName())
                            .children(new ArrayList<>())
                            .build());
                }
            }
        }
        for(BallMenu ballMenu:all){
            if(ballMenu.getParentId()==0){
                continue;
            }
            //
            for(Menu menu:tree){
                //获得第一层子菜单
                List<Menu> children1 = menu.getChildren();
                for(Menu child:children1){
                    if(child.getId().equals(ballMenu.getParentId())){
                        //放入三级
                        List<Menu> last = child.getChildren();
                        last.add(Menu.builder()
                                .id(ballMenu.getId())
                                .label(ballMenu.getMenuName())
                                .children(new ArrayList<>())
                                .build());
                    }
                }
            }
        }
        Collections.sort(tree, Comparator.comparingInt(Menu::getSort));
        return tree;
    }

    @PostMapping("loss")
    public Object perloss(@RequestParam("json") String json){
        try {
            ApiFixtures apiFixtures = JsonUtil.fromJson(json, ApiFixtures.class);
            List<ApiFixtures.FixtureReponse> resposneList = apiFixtures.getResponse();
            if(resposneList==null||resposneList.isEmpty()){
                return BaseResponse.failedWithMsg("failed");
            }
            for (ApiFixtures.FixtureReponse response : resposneList) {
                ApiFixture fixture = response.getFixture();
                ApiTeams teams = response.getTeams();
                ApiGoals goals = response.getGoals();
                ApiScore score = response.getScore();
                ApiLeague league = response.getLeague();
                //比赛是否结束,如果结束查数据库,有的话修改状态 并结算订单
                switch (fixture.getStatus().getShortStatus()) {
                    case ApiFixtureStatus.STATUS_TBD:
                    case ApiFixtureStatus.STATUS_NS:
                        //初始保存
                        try {
                            BallGame insert = BallGame.builder()
                                    .id(fixture.getId())
                                    .gameStatus(1)
                                    .status(1)
                                    .createdAt(TimeUtil.getNowTimeMill())
                                    .allianceLogo(league.getLogo())
                                    .allianceName(league.getName())
                                    .mainLogo(teams.getHome().getLogo())
                                    .mainName(teams.getHome().getName())
                                    .guestLogo(teams.getAway().getLogo())
                                    .guestName(teams.getAway().getName())
                                    .startTime(fixture.getTimestamp() * 1000)
                                    .build();
                            if (gameService.insert(insert)) {
                                //存入redis,然后去查赔率
                                redisUtil.leftSet(RedisKeyContant.GAME_NEED_QUERY_ODDS, insert);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case ApiFixtureStatus.STATUS_FT:
                    case ApiFixtureStatus.STATUS_PEN:
                        //比赛结束
                        gameService.edit(BallGame.builder()
                                .id(fixture.getId())
                                .homeHalf(score.getHalftime().getHome())
                                .guestHalf(score.getHalftime().getAway())
                                .homeFull(goals.getHome())
                                .guestFull(goals.getAway())
                                .homeOvertime(score.getExtratime().getHome())
                                .guestOvertime(score.getExtratime().getAway())
                                .homePenalty(score.getPenalty().getHome())
                                .guestPenalty(score.getPenalty().getAway())
                                .gameStatus(3)
                                .build());
                        ThreadPoolUtil.exec(() -> ballBetService.betOpen(fixture.getId()));
                        break;
                    case ApiFixtureStatus.STATUS_CANC:
                    case ApiFixtureStatus.STATUS_ABD:
                    case ApiFixtureStatus.STATUS_AWD:
                        //比赛取消
                        break;
                    case ApiFixtureStatus.STATUS_WO:
                        //走过场
                        break;
                    default:
                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return BaseResponse.successWithMsg("ok");
    }

    private void createPerLoss(BallGame game, List<ApiBookmakers.ApiBetItem> values, List<BallGameLossPerCent> lossPerCents, int gameType) {
        for(ApiBookmakers.ApiBetItem item:values){
            String score = item.getValue();
            String[] split = score.split(":");
            BallGameLossPerCent add = BallGameLossPerCent.builder()
                    .lossPerCent(item.getOdd())
                    .scoreHome(split[0])
                    .scoreAway(split[1])
                    .antiPerCent(BigDecimalUtil.antiPerCent(item.getOdd()))
                    .even(2)
                    .status(1)
                    .gameId(game.getId())
                    .gameType(gameType)
                    .build();
            lossPerCents.add(add);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class Menu{
//        id: 9,
//        label: '系统管理',
//        children
        private Long id;
        private String label;
        private List<Menu> children;
        @JsonIgnore
        private Integer sort;
    }
}
