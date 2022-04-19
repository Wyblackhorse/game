package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallSlideshow;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallSlideshowMapper;
import com.oxo.ball.service.admin.IBallSlideshowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 轮播图 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallSlideshowServiceImpl extends ServiceImpl<BallSlideshowMapper, BallSlideshow> implements IBallSlideshowService {

    @Override
    public SearchResponse<BallSlideshow> search(BallSlideshow queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallSlideshow> response = new SearchResponse<>();
        Page<BallSlideshow> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallSlideshow> query = new QueryWrapper<>();
        if(queryParam.getStatus()!=null){
            query.eq("status",queryParam.getStatus());
        }
        IPage<BallSlideshow> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallSlideshow insert(BallSlideshow slideshow) {
        boolean save = save(slideshow);
        return slideshow;
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean edit(BallSlideshow slideshow) {
        return updateById(slideshow);
    }

    @Override
    public Boolean status(BallSlideshow slideshow) {
        BallSlideshow edit = BallSlideshow.builder()
                .status(slideshow.getStatus())
                .build();
        edit.setId(slideshow.getId());
        return updateById(edit);
    }
}
