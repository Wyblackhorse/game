package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallAnnouncement;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallAnnouncementMapper;
import com.oxo.ball.service.admin.IBallAnnouncementService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 轮播公告 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallAnnouncementServiceImpl extends ServiceImpl<BallAnnouncementMapper, BallAnnouncement> implements IBallAnnouncementService {
    @Override
    public SearchResponse<BallAnnouncement> search(BallAnnouncement queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallAnnouncement> response = new SearchResponse<>();
        Page<BallAnnouncement> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallAnnouncement> query = new QueryWrapper<>();
        if(queryParam.getStatus()!=null){
            query.eq("status",queryParam.getStatus());
        }
        IPage<BallAnnouncement> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallAnnouncement insert(BallAnnouncement announcement) {
        boolean save = save(announcement);
        return announcement;
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean edit(BallAnnouncement announcement) {
        return updateById(announcement);
    }

    @Override
    public Boolean status(BallAnnouncement announcement) {
        BallAnnouncement edit = BallAnnouncement.builder()
                .status(announcement.getStatus())
                .build();
        edit.setId(announcement.getId());
        return updateById(edit);
    }
}
