package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallVip;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallVipMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.service.admin.IBallVipService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-24
 */
@Service
public class BallVipServiceImpl extends ServiceImpl<BallVipMapper, BallVip> implements IBallVipService {
    @Override
    public SearchResponse<BallVip> search(BallVip queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallVip> response = new SearchResponse<>();
        Page<BallVip> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallVip> query = new QueryWrapper<>();
        if(queryParam.getStatus()!=null){
            query.eq("status",queryParam.getStatus());
        }
        IPage<BallVip> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallVip insert(BallVip ballVip) {
        ballVip.setStatus(1);
        boolean save = save(ballVip);
        return ballVip;
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean edit(BallVip ballVip) {
        return updateById(ballVip);
    }

    @Override
    public Boolean status(BallVip notice) {
        BallVip edit = BallVip.builder()
                .status(notice.getStatus())
                .build();
        edit.setId(notice.getId());
        return updateById(edit);
    }
}
