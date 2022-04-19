package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallCommissionStrategy;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallCommissionStrategyMapper;
import com.oxo.ball.service.admin.IBallCommissionStrategyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;

/**
 * <p>
 * 反佣策略 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallCommissionStrategyServiceImpl extends ServiceImpl<BallCommissionStrategyMapper, BallCommissionStrategy> implements IBallCommissionStrategyService {
    @Override
    public SearchResponse<BallCommissionStrategy> search(BallCommissionStrategy queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallCommissionStrategy> response = new SearchResponse<>();
        Page<BallCommissionStrategy> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallCommissionStrategy> query = new QueryWrapper<>();
        if(queryParam.getStatus()!=null){
            query.eq("status",queryParam.getStatus());
        }
        IPage<BallCommissionStrategy> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallCommissionStrategy insert(BallCommissionStrategy commissionStrategyService) {
        boolean save = save(commissionStrategyService);
        return commissionStrategyService;
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean edit(BallCommissionStrategy commissionStrategyService) {
        return updateById(commissionStrategyService);
    }

    @Override
    public Boolean status(BallCommissionStrategy slideshow) {
        BallCommissionStrategy edit = BallCommissionStrategy.builder()
                .status(slideshow.getStatus())
                .build();
        edit.setId(slideshow.getId());
        return updateById(edit);
    }
}
