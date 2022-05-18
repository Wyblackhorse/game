package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallLoggerBet;
import com.oxo.ball.bean.dao.BallLoggerBet;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallLoggerBetMapper;
import com.oxo.ball.mapper.BallLoggerMapper;
import com.oxo.ball.service.admin.IBallLoggerBetService;
import com.oxo.ball.service.admin.IBallLoggerService;
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
public class BallLoggerBetServiceImpl extends ServiceImpl<BallLoggerBetMapper, BallLoggerBet> implements IBallLoggerBetService {

    @Override
    public SearchResponse<BallLoggerBet> search(BallLoggerBet queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallLoggerBet> response = new SearchResponse<>();
        Page<BallLoggerBet> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallLoggerBet> query = new QueryWrapper<>();
        query.orderByDesc("id");
        IPage<BallLoggerBet> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallLoggerBet insert(BallLoggerBet loggerBet) {
        save(loggerBet);
        return loggerBet;
    }
}
