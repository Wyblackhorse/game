package com.oxo.ball.service.player;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dao.BallBet;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.BetRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;

import java.sql.SQLException;

public interface IPlayerBetService extends IService<BallBet> {
    BaseResponse gameBet(BetRequest betRequest, BallPlayer playerId) throws SQLException;
}
