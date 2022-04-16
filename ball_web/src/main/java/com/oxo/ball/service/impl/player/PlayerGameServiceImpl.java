package com.oxo.ball.service.impl.player;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dao.BallSystemConfig;
import com.oxo.ball.bean.dto.req.player.GameRequest;
import com.oxo.ball.bean.dto.req.player.PlayerAuthLoginRequest;
import com.oxo.ball.bean.dto.req.player.PlayerRegistRequest;
import com.oxo.ball.bean.dto.resp.AuthLoginResponse;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.contant.RedisKeyContant;
import com.oxo.ball.mapper.BallGameMapper;
import com.oxo.ball.mapper.BallPlayerMapper;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerGameService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.*;
import io.undertow.util.StatusCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 玩家 -赛事
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class PlayerGameServiceImpl extends ServiceImpl<BallGameMapper, BallGame> implements IPlayerGameService {
    @Override
    @Cacheable(value = "ball_player_game_by_id", key = "#id", unless = "#result == null")
    public BallGame findById(Long id) {
        return getById(id);
    }

    @Override
    public SearchResponse<BallGame> search(GameRequest queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallGame> response = new SearchResponse<>();
        Page<BallGame> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallGame> query = new QueryWrapper<>();
        /**
         * 过滤条件
         * 一.未开始或者未结束赛事
         *      status: 0 未开始,或者未结束
         *      startTime: 0 全部 1今天 2明天
         * 二.已结束赛事
         * 1.全部赛事，直接按ID排序
         *      status: 1已结束
         *      startTime: 0 全部 1今天 2昨天
         * 排序条件
         * top 1.置顶
         */
        if(queryParam.getStartTime()==1){
            //today
            query.ge("start_time",TimeUtil.getDayBegin().getTime());
            query.le("start_time",TimeUtil.getDayEnd().getTime());
        }
        if(queryParam.getStatus()==0){
            //未开始,未
            query.ne("status",3);
            //未开始查明天
            if(queryParam.getStartTime()==2){
                query.ge("start_time",TimeUtil.getBeginDayOfTomorrow().getTime());
                query.le("start_time",TimeUtil.getEndDayOfTomorrow().getTime());
            }
        }else{
            query.eq("status",3);
            //已结束查昨天
            if(queryParam.getStartTime()==2){
                query.ge("start_time",TimeUtil.getBeginDayOfYesterday().getTime());
                query.le("start_time",TimeUtil.getEndDayOfYesterday().getTime());
            }
        }
        //先ID降序再top升序
        query.orderByDesc("id");
        query.orderByAsc("top");
        IPage<BallGame> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }
}
