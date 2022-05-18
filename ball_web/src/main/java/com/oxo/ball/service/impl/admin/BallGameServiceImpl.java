package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallBet;
import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallGameMapper;
import com.oxo.ball.service.admin.IBallBetService;
import com.oxo.ball.service.admin.IBallGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.service.player.IPlayerGameService;
import com.oxo.ball.utils.TimeUtil;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * <p>
 * 游戏赛事 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallGameServiceImpl extends ServiceImpl<BallGameMapper, BallGame> implements IBallGameService {

    @Autowired
    IPlayerGameService playerGameService;
    @Autowired
    IBallBetService ballBetService;

    @Override
    public SearchResponse<BallGame> search(BallGame queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallGame> response = new SearchResponse<>();
        Page<BallGame> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallGame> query = new QueryWrapper<>();
        if(queryParam.getHot()!=null){
            query.eq("hot",queryParam.getHot());
        }
        if(queryParam.getGameStatus()!=null){
            if(queryParam.getGameStatus()==0){
                //未结算的赛事
                query.in("game_status",new Integer[]{1,2});
            }else{
                //已结算
                query.eq("game_status",3);
            }
        }
        //先ID降序再top升序
        query.orderByDesc("id");
        IPage<BallGame> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public Boolean insert(BallGame ballGame) {
        return save(ballGame);
    }

    @Override
    @CacheEvict(value = "ball_player_game_by_id", key = "#ballGame.getId()")
    public BaseResponse edit(BallGame ballGame) {
        BallGame edit = BallGame.builder()
                .homeFull(ballGame.getHomeFull())
                .homeHalf(ballGame.getHomeHalf())
                .homeOvertime(ballGame.getHomeOvertime())
                .homePenalty(ballGame.getHomePenalty())
                .guestFull(ballGame.getGuestFull())
                .guestHalf(ballGame.getGuestHalf())
                .guestOvertime(ballGame.getGuestOvertime())
                .guestPenalty(ballGame.getGuestPenalty())
                .build();
        edit.setId(ballGame.getId());
        boolean b = updateById(edit);

        return b?BaseResponse.successWithMsg("ok"):BaseResponse.failedWithMsg("edit failed");
    }

    @Override
    public void whenGameStart() {
        //时间到的设置为正常进行
        UpdateWrapper update = new UpdateWrapper();
        update.lt("start_time",TimeUtil.getNowTimeMill());
        update.eq("game_status",1);
        BallGame edit = BallGame.builder()
                .gameStatus(2)
                .build();
        update(edit,update);
    }

    @Override
    @CacheEvict(value = "ball_player_game_by_id", key = "#ballGame.getId()")
    public Boolean editStatus(BallGame ballGame) {
        BallGame edit = BallGame.builder()
                .status(ballGame.getStatus()==1?2:1)
                .build();
        edit.setId(ballGame.getId());
        edit.setUpdatedAt(TimeUtil.getNowTimeMill());
        return updateById(edit);
    }

    @Override
    @CacheEvict(value = "ball_player_game_by_id", key = "#ballGame.getId()")
    public Boolean editStatusTop(BallGame ballGame) {
        BallGame edit = BallGame.builder()
                .top(ballGame.getTop()==1?2:1)
                .build();
        edit.setId(ballGame.getId());
        edit.setUpdatedAt(TimeUtil.getNowTimeMill());
        return updateById(edit);
    }

    @Override
    @CacheEvict(value = "ball_player_game_by_id", key = "#ballGame.getId()")
    public Boolean editStatusHot(BallGame ballGame) {
        BallGame edit = BallGame.builder()
                .hot(ballGame.getHot()==1?2:1)
                .build();
        edit.setId(ballGame.getId());
        edit.setUpdatedAt(TimeUtil.getNowTimeMill());
        return updateById(edit);
    }

    @Override
    @CacheEvict(value = "ball_player_game_by_id", key = "#ballGame.getId()")
    public Boolean editStatusEven(BallGame ballGame) {
        BallGame edit = BallGame.builder()
                .even(ballGame.getEven()==1?2:1)
                .build();
        edit.setId(ballGame.getId());
        edit.setUpdatedAt(TimeUtil.getNowTimeMill());
        return updateById(edit);
    }

    @PostMapping("info")
    public Object info(@RequestBody BallGame ballGame){
        BallGame res = playerGameService.findById(ballGame.getId());
        return BaseResponse.successWithData(res);
    }

    @Override
    public BaseResponse recount(BallGame ballGame) {
        BaseResponse res = ballBetService.betRecount(ballGame.getId());
        return res;
    }

    @Override
    public BaseResponse rollback(BallGame ballGame) {
        BaseResponse res = ballBetService.betRollback(ballGame.getId());
        return res;
    }
}
