package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallSystemNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 * 系统公告 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallSystemNoticeService extends IService<BallSystemNotice> {
    SearchResponse<BallSystemNotice> search(BallSystemNotice query, Integer pageNo, Integer pageSize);
    BallSystemNotice insert(BallSystemNotice notice);
    Boolean delete(Long id);
    Boolean edit(BallSystemNotice notice);
    Boolean status(BallSystemNotice notice);
}
