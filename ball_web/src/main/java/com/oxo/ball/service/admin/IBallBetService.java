package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallBet;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;

import java.util.List;

/**
 * <p>
 * 下注 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallBetService extends IService<BallBet> {
    SearchResponse<BallBet> search(BallBet query, Integer pageNo, Integer pageSize);
    BallBet findById(Long id);
    List<BallBet> findByGameId(Long id,int status);
    Boolean edit(BallBet ballBet);

    BaseResponse undo(BallBet query);

    BaseResponse info(BallBet query);

    void betOpen(Long id);

    BaseResponse betRecount(Long id);

    BaseResponse betRollback(Long id);
}
