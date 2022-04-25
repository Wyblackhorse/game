package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallVip;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-24
 */
public interface IBallVipService extends IService<BallVip> {
    SearchResponse<BallVip> search(BallVip query, Integer pageNo, Integer pageSize);
    BallVip insert(BallVip ballVip);
    Boolean delete(Long id);
    Boolean edit(BallVip ballVip);
    Boolean status(BallVip ballVip);
}
