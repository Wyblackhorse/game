package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallLoggerBack;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallLoggerBackMapper;
import com.oxo.ball.service.admin.IBallLoggerBackService;
import com.oxo.ball.service.admin.IBallLoggerBackService;
import com.oxo.ball.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 日志表 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallLoggerBackServiceImpl extends ServiceImpl<BallLoggerBackMapper, BallLoggerBack> implements IBallLoggerBackService {

    @Resource
    BallLoggerBackMapper mapper;

    @Override
    public SearchResponse<BallLoggerBack> search(BallPlayer currPlayer, Integer pageNo, Integer pageSize) {
        SearchResponse<BallLoggerBack> response = new SearchResponse<>();
        Page<BallLoggerBack> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallLoggerBack> query = new QueryWrapper<>();
        query.eq("player_id",currPlayer.getId());
        query.orderByDesc("id");
        IPage<BallLoggerBack> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BaseResponse statis(BallPlayer currPlayer) {
//        今日 昨日 上周
//                - 列表 日期 返佣类型 返佣金额 操作
        Map<String,Object> data = new HashMap<>();
        Map<String, Object> statis = statis(currPlayer.getId(), TimeUtil.getDayBegin().getTime(), TimeUtil.getDayEnd().getTime());
        data.put("today",statis==null?0:statis.get("money"));
        statis = statis(currPlayer.getId(), TimeUtil.getBeginDayOfYesterday().getTime(), TimeUtil.getEndDayOfYesterday().getTime());
        data.put("yesterday",statis==null?0:statis.get("money"));
        statis = statis(currPlayer.getId(), TimeUtil.getBeginDayOfLastWeek().getTime(), TimeUtil.getEndDayOfLastWeek().getTime());
        data.put("lastWeek",statis==null?0:statis.get("money"));
        return BaseResponse.successWithData(data);
    }

    private Map<String,Object> statis(Long pid,Long begin,Long end){
        return mapper.statis(pid,begin,end);
    }

    @Override
    public BallLoggerBack insert(BallLoggerBack loggerBet) {
        save(loggerBet);
        return loggerBet;
    }
}
