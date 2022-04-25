package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallLoggerLogin;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallLoggerMapper;
import com.oxo.ball.service.admin.IBallLoggerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class BallLoggerServiceImpl extends ServiceImpl<BallLoggerMapper, BallLoggerLogin> implements IBallLoggerService {

    @Override
    public SearchResponse<BallLoggerLogin> search(BallLoggerLogin queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallLoggerLogin> response = new SearchResponse<>();
        Page<BallLoggerLogin> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallLoggerLogin> query = new QueryWrapper<>();
        IPage<BallLoggerLogin> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallLoggerLogin insert(BallLoggerLogin loggerLogin) {
        save(loggerLogin);
        return loggerLogin;
    }
}
