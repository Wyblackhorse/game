package com.oxo.ball.service.player;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dao.BallBet;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.BetPreRequest;
import com.oxo.ball.bean.dto.req.player.BetRequest;
import com.oxo.ball.bean.dto.req.player.PlayerBetRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;

import java.sql.SQLException;

public interface IPlayerBetService extends IService<BallBet> {
    BaseResponse gameBet(BetRequest betRequest, BallPlayer playerId) throws SQLException;
    SearchResponse<BallBet> search(PlayerBetRequest queryParam, BallPlayer ballPlayer, Integer pageNo, Integer pageSize) ;
    Long getDayOrderNo();
    void clearDayOrderNo();
    BaseResponse gameBetPrepare(BetPreRequest betRequest, BallPlayer currentPlayer);
    BaseResponse unbet(Long betId, BallPlayer currentUser);

    BaseResponse betInfo(Long betId, BallPlayer currentUser);

    boolean edit(BallBet edit);
}
