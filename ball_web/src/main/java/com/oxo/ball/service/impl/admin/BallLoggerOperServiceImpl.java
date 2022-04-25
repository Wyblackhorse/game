package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallLoggerOper;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallLoggerOperMapper;
import com.oxo.ball.service.admin.IBallLoggerOperService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallLoggerOperServiceImpl extends ServiceImpl<BallLoggerOperMapper, BallLoggerOper> implements IBallLoggerOperService {

    @Override
    public SearchResponse<BallLoggerOper> search(BallLoggerOper queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallLoggerOper> response = new SearchResponse<>();
        Page<BallLoggerOper> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallLoggerOper> query = new QueryWrapper<>();
        query.orderByDesc("id");
        IPage<BallLoggerOper> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallLoggerOper insert(BallLoggerOper loggerOper) {
        save(loggerOper);
        return loggerOper;
    }
}
