package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.oxo.ball.bean.dao.BallGameLossPerCent;
import com.oxo.ball.mapper.BallGameLossPerCentMapper;
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 游戏赔率 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallGameLossPerCentServiceImpl extends ServiceImpl<BallGameLossPerCentMapper, BallGameLossPerCent> implements IBallGameLossPerCentService {

    @Override
    @Cacheable(value = "ball_game_loss_per_cent_by_game_id", key = "'_ID_' + #gameId", unless = "#result == null")
    public List<BallGameLossPerCent> findByGameId(Long gameId) {
        QueryWrapper query = new QueryWrapper();
        query.eq("game_id", gameId);
        List list = list(query);
        return list;
    }

    @Override
    @Cacheable(value = "ball_game_loss_per_cent_by_id", key = "'_ID_' + #oddsId", unless = "#result == null")
    public BallGameLossPerCent findById(Long oddsId) {
        return getById(oddsId);
    }
}
