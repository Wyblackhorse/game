package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallBet;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallBetMapper;
import com.oxo.ball.service.admin.IBallBetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 下注 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallBetServiceImpl extends ServiceImpl<BallBetMapper, BallBet> implements IBallBetService {

    @Override
    public SearchResponse<BallBet> search(BallBet queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallBet> response = new SearchResponse<>();
        Page<BallBet> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallBet> query = new QueryWrapper<>();
        //先ID降序再top升序
        query.orderByDesc("id");
        IPage<BallBet> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }
}
