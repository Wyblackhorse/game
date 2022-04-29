package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallSystemNotice;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallSystemNoticeMapper;
import com.oxo.ball.service.admin.IBallSystemNoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统公告 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallSystemNoticeServiceImpl extends ServiceImpl<BallSystemNoticeMapper, BallSystemNotice> implements IBallSystemNoticeService {
    @Override
    public SearchResponse<BallSystemNotice> search(BallSystemNotice queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallSystemNotice> response = new SearchResponse<>();
        Page<BallSystemNotice> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallSystemNotice> query = new QueryWrapper<>();
        if(queryParam.getStatus()!=null){
            query.eq("status",queryParam.getStatus());
        }
        IPage<BallSystemNotice> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallSystemNotice insert(BallSystemNotice slideshow) {
        slideshow.setStatus(1);
        slideshow.setCreatedAt(System.currentTimeMillis());
        boolean save = save(slideshow);
        return slideshow;
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean edit(BallSystemNotice slideshow) {
        return updateById(slideshow);
    }

    @Override
    public Boolean status(BallSystemNotice notice) {
        BallSystemNotice edit = BallSystemNotice.builder()
                .status(notice.getStatus())
                .build();
        edit.setId(notice.getId());
        return updateById(edit);
    }
}
