package com.oxo.ball.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dao.BallLoggerBet;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallLoggerBetService extends IService<BallLoggerBet> {
    SearchResponse<BallLoggerBet> search(BallLoggerBet queryParam, Integer pageNo, Integer pageSize);
    BallLoggerBet insert(BallLoggerBet announcement);
}
