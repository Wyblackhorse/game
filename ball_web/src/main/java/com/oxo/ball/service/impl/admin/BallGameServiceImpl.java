package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallGameMapper;
import com.oxo.ball.service.admin.IBallGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏赛事 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallGameServiceImpl extends ServiceImpl<BallGameMapper, BallGame> implements IBallGameService {

    @Override
    public SearchResponse<BallGame> search(BallGame queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallGame> response = new SearchResponse<>();
        Page<BallGame> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallGame> query = new QueryWrapper<>();
        //先ID降序再top升序
        query.orderByDesc("id");
        IPage<BallGame> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }
}
