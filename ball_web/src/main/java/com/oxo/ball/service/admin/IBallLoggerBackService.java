package com.oxo.ball.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dao.BallLoggerBack;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 * 日志表 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallLoggerBackService extends IService<BallLoggerBack> {
    SearchResponse<BallLoggerBack> search(BallPlayer currPlayer, Integer pageNo, Integer pageSize);
    BaseResponse statis(BallPlayer currPlayer);
    BallLoggerBack insert(BallLoggerBack ballLoggerBack);
}
