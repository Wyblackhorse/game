package com.oxo.ball.service.impl.admin;

import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallGameLossPerCent;
import com.oxo.ball.bean.dto.resp.api.*;
import com.oxo.ball.config.SomeConfig;
import com.oxo.ball.contant.LogsContant;
import com.oxo.ball.contant.RedisKeyContant;
import com.oxo.ball.service.admin.IApiService;
import com.oxo.ball.service.admin.IBallBetService;
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import com.oxo.ball.service.admin.IBallGameService;
import com.oxo.ball.service.player.IPlayerGameService;
import com.oxo.ball.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ApiServiceImpl implements IApiService {
    private Logger apiLog = LoggerFactory.getLogger(LogsContant.API_LOG);

    @Autowired
    SomeConfig someConfig;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IBallGameService gameService;
    @Autowired
    IPlayerGameService playerGameService;
    @Autowired
    IBallBetService betService;
    @Autowired
    IBallGameLossPerCentService lossPerCentService;


    /**
     * 每小时更新一次
     * 放redis
     */
    @Override
    public void refreshLeagues() {
        //获取当前yyyy和上一yyyy,
        //获取联赛
        // https://v3.football.api-sports.io/leagues?current=true&season=2022

        Object leaguesQueryed = redisUtil.get(RedisKeyContant.LEAGUES_HAS_QUERY);
        if (leaguesQueryed != null) {
            return;
        }

        Integer nowYear = TimeUtil.getNowYear();
        List<Long> leagues = someConfig.getApiLeagues();
        String res = HttpUtil.doGet("https://v3.football.api-sports.io/leagues?id="+leagues.get(0)+"&current=true&season=" + nowYear, doRequestHeader(someConfig.getApiKey()));
        apiLog.info("leagues:{}",res);
        try {
            ApiLeagues apiLeagues = JsonUtil.fromJson(res, ApiLeagues.class);
            List<ApiLeagues.LeaguesResponse> response = apiLeagues.getResponse();
            if (response == null || response.isEmpty()) {
                return;
            }
            redisUtil.listSet(RedisKeyContant.LEAGUES_LIST, response);
            //保持1小时更新
            redisUtil.set(RedisKeyContant.LEAGUES_HAS_QUERY, 1, TimeUtil.TIME_EIGHT_HOURS / 1000 - 30);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新赛事比分-每分钟更新一次
     * 1.redis中读取联赛去查询赛事,如果redis中没有就查本地数据库的赛事
     */
    @Override
    public void refreshFixtures() {
        // https://v3.football.api-sports.io/fixtures?live=all&league=
        long lsize = redisUtil.lGetListSize(RedisKeyContant.LEAGUES_LIST);
        if (lsize > 0) {
            while (true) {
                Object lpop = redisUtil.lpop(RedisKeyContant.LEAGUES_LIST);
                if (lpop == null) {
                    break;
                }
                try {
                    ApiLeagues.LeaguesResponse leaguesResponse = (ApiLeagues.LeaguesResponse) lpop;
                    ApiLeague league = leaguesResponse.getLeague();
                    String res = HttpUtil.doGet("https://v3.football.api-sports.io/fixtures?live=all&league=" + league.getId(),
                            doRequestHeader(someConfig.getApiKey()));
                    apiLog.info("fixture:{}",res);
                    ApiFixtures apiFixtures = JsonUtil.fromJson(res, ApiFixtures.class);
                    List<ApiFixtures.FixtureReponse> resposneList = apiFixtures.getResponse();
                    if(resposneList==null||resposneList.isEmpty()){
                        continue;
                    }
                    for (ApiFixtures.FixtureReponse response : resposneList) {
                        ApiFixture fixture = response.getFixture();
                        ApiTeams teams = response.getTeams();
                        ApiGoals goals = response.getGoals();
                        ApiScore score = response.getScore();
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
                                ThreadPoolUtil.exec(() -> betService.betOpen(fixture.getId()));
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
            }
            return;
        } else {
            //查询未结束比赛
            List<BallGame> unfinish = playerGameService.findUnfinish();
            for (BallGame game : unfinish) {
                try {
                    String res = HttpUtil.doGet("https://v3.football.api-sports.io/fixtures?id="+game.getId(),
                            doRequestHeader(someConfig.getApiKey()));
                    apiLog.info("db_fixture:{}",res);
                    ApiFixtures apiFixtures = JsonUtil.fromJson(res, ApiFixtures.class);
                    List<ApiFixtures.FixtureReponse> resposneList = apiFixtures.getResponse();
                    if(resposneList==null||resposneList.isEmpty()){
                        continue;
                    }
                    for (ApiFixtures.FixtureReponse response : resposneList) {
                        ApiFixture fixture = response.getFixture();
                        ApiGoals goals = response.getGoals();
                        ApiScore score = response.getScore();
                        //比赛是否结束,如果结束查数据库,有的话修改状态 并结算订单
                        switch (fixture.getStatus().getShortStatus()) {
                            case ApiFixtureStatus.STATUS_FT:
                            case ApiFixtureStatus.STATUS_PEN:
                                //比赛结束
                                gameService.edit(BallGame.builder()
                                        .id(game.getId())
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
                                ThreadPoolUtil.exec(() -> betService.betOpen(game.getId()));
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    @Override
    public void refreshFixturesAll() {
        try {
            String res = HttpUtil.doGet("https://v3.football.api-sports.io/fixtures?live=all",
                    doRequestHeader(someConfig.getApiKey()));
            apiLog.info("fixture:{}",res);
            ApiFixtures apiFixtures = JsonUtil.fromJson(res, ApiFixtures.class);
            List<ApiFixtures.FixtureReponse> resposneList = apiFixtures.getResponse();
            if(resposneList==null||resposneList.isEmpty()){
                return;
            }
            for (ApiFixtures.FixtureReponse response : resposneList) {
                ApiFixture fixture = response.getFixture();
                ApiTeams teams = response.getTeams();
                ApiGoals goals = response.getGoals();
                ApiScore score = response.getScore();
                ApiLeague league = response.getLeague();
                BallGame game = BallGame.builder()
                        .id(fixture.getId())
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
                //比赛是否结束,如果结束查数据库,有的话修改状态 并结算订单
                switch (fixture.getStatus().getShortStatus()) {
                    case ApiFixtureStatus.STATUS_TBD:
                    case ApiFixtureStatus.STATUS_NS:
                        game.setGameStatus(1);
                        break;
                    case ApiFixtureStatus.STATUS_FT:
                    case ApiFixtureStatus.STATUS_PEN:
                        game.setGameStatus(3);
                        break;
                    case ApiFixtureStatus.STATUS_CANC:
                    case ApiFixtureStatus.STATUS_ABD:
                    case ApiFixtureStatus.STATUS_AWD:
                        //比赛取消
                        game.setStatus(2);
                        break;
                    case ApiFixtureStatus.STATUS_WO:
                        //走过场
                        game.setStatus(2);
                        break;
                    default:
                        game.setGameStatus(2);
                        break;
                }
                //初始保存
                try {
                    if (gameService.insert(game)) {
                        //存入redis,然后去查赔率
                        redisUtil.leftSet(RedisKeyContant.GAME_NEED_QUERY_ODDS, game);
                    }
                } catch (Exception ex) {
                    //已经保存了，修改状态
                    if(game.getGameStatus()==3){
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
                        ThreadPoolUtil.exec(() -> betService.betOpen(fixture.getId()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void refreshOdds() {
        // https://v3.football.api-sports.io/odds?bookmaker=&fixture=
        Object lpop = redisUtil.lpop(RedisKeyContant.GAME_NEED_QUERY_ODDS);
        if (lpop == null) {
            return;
        }
        BallGame game = (BallGame) lpop;
        String res = HttpUtil.doGet("https://v3.football.api-sports.io/odds?bookmaker=" + someConfig.getApiBookmaker() + "&fixture=" + game.getId(),
                doRequestHeader(someConfig.getApiKey()));
        apiLog.info("odds:{}",res);
        try {
            ApiOdds apiOdds = JsonUtil.fromJson(res, ApiOdds.class);
            List<ApiOdds.ApiOddsResponse> response = apiOdds.getResponse();
            if(response==null||response.isEmpty()){
                return ;
            }
            List<ApiBookmakers> bookmakers = response.get(0).getBookmakers();
            if(bookmakers==null||bookmakers.isEmpty()){
                return ;
            }
            ApiBookmakers apiBookmakers = bookmakers.get(0);
            List<ApiBookmakers.ApiBets> bets = apiBookmakers.getBets();
            for(ApiBookmakers.ApiBets bet:bets){
                if(bet.getName().equals("Exact Score")){
                    //全场比分和赔率
                    List<ApiBookmakers.ApiBetItem> values = bet.getValues();
                    List<BallGameLossPerCent> lossPerCents = new ArrayList<>();
                    createPerLoss(game, values, lossPerCents,2);
                    lossPerCentService.batchInsert(lossPerCents);
                }
                if(bet.getName().equals("Correct Score - First Half")){
                    List<ApiBookmakers.ApiBetItem> values = bet.getValues();
                    List<BallGameLossPerCent> lossPerCents = new ArrayList<>();
                    createPerLoss(game, values, lossPerCents,1);
                    lossPerCentService.batchInsert(lossPerCents);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    private Map<String, String> doRequestHeader(String key) {
        Map<String, String> header = new HashMap<>();
        header.put("x-rapidapi-host", "v3.football.api-sports.io");
        header.put("x-rapidapi-key", key);
        return header;
    }
}
