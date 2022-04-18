package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallBalanceChange;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.BalanceChangeRequest;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 * 账变表 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallBalanceChangeService extends IService<BallBalanceChange> {

    SearchResponse<BallBalanceChange> search(BallPlayer currentUser, BalanceChangeRequest balanceChangeRequest, Integer pageNo, Integer pageSize);
    boolean insert(BallBalanceChange balanceChange);
}
