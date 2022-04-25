package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallLoggerLogin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallLoggerService extends IService<BallLoggerLogin> {
    SearchResponse<BallLoggerLogin> search(BallLoggerLogin queryParam, Integer pageNo, Integer pageSize);
    BallLoggerLogin insert(BallLoggerLogin announcement);
}
