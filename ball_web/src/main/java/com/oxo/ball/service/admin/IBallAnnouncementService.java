package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallAnnouncement;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 * 轮播公告 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallAnnouncementService extends IService<BallAnnouncement> {
    SearchResponse<BallAnnouncement> search(BallAnnouncement queryParam, Integer pageNo, Integer pageSize);
    BallAnnouncement insert(BallAnnouncement announcement);
    Boolean delete(Long id);
    Boolean edit(BallAnnouncement announcement);
    Boolean status(BallAnnouncement announcement);
}
