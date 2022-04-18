package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.BalanceChangeRequest;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallBalanceChangeMapper;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账变表 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallBalanceChangeServiceImpl extends ServiceImpl<BallBalanceChangeMapper, BallBalanceChange> implements IBallBalanceChangeService {

    @Override
    public SearchResponse<BallBalanceChange> search(BallPlayer currentUser, BalanceChangeRequest balanceChangeRequest, Integer pageNo, Integer pageSize) {
        SearchResponse<BallBalanceChange> response = new SearchResponse<>();

        Page<BallBalanceChange> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallBalanceChange> query = new QueryWrapper<>();
        if(currentUser!=null){
            query.eq("player_id",currentUser.getId());
        }
        if(balanceChangeRequest.getType()!=null){
            query.eq("balanceChange_type",balanceChangeRequest.getType());
        }
        query.orderByDesc("id");
        IPage<BallBalanceChange> pages = page(page, query);

        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public boolean insert(BallBalanceChange balanceChange) {
        return save(balanceChange);
    }
}
