package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallGameLossPerCent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 游戏赔率 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallGameLossPerCentService extends IService<BallGameLossPerCent> {

    List<BallGameLossPerCent> findByGameId(Long gameId);

    BallGameLossPerCent findById(Long oddsId);
}
