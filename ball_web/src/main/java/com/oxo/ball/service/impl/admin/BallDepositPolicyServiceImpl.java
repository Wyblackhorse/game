package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oxo.ball.bean.dao.BallDepositPolicy;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallDepositPolicyMapper;
import com.oxo.ball.service.admin.IBallDepositPolicyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.utils.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

/**
 * <p>
 * 存款策略 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class BallDepositPolicyServiceImpl extends ServiceImpl<BallDepositPolicyMapper, BallDepositPolicy> implements IBallDepositPolicyService {
    @Override
    public SearchResponse<BallDepositPolicy> search(BallDepositPolicy queryParam, Integer pageNo, Integer pageSize) {
        SearchResponse<BallDepositPolicy> response = new SearchResponse<>();
        Page<BallDepositPolicy> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallDepositPolicy> query = new QueryWrapper<>();
        if(queryParam.getStatus()!=null){
            query.eq("status",queryParam.getStatus());
        }
        if(queryParam.getDepositPolicyType()!=null){
            query.eq("deposit_policy_type",queryParam.getDepositPolicyType());
        }
        IPage<BallDepositPolicy> pages = page(page, query);
        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());
        return response;
    }

    @Override
    public BallDepositPolicy getCurrentDepositPolicy(int type) {
        QueryWrapper<BallDepositPolicy> query = new QueryWrapper<>();
        query.eq("status",1);
        query.eq("deposit_policy_type",type);
        //当前可用的,小于结束时间
        query.le("end_time",TimeUtil.getNowTimeMill());
        //大于开始时间
        query.gt("start_time",TimeUtil.getNowTimeMill());
        query.orderByDesc("preferential_per");
        List<BallDepositPolicy> list = list(query);
        if(list!=null&&!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public BallDepositPolicy insert(BallDepositPolicy depositPolicy) throws ParseException {
        if(!StringUtils.isEmpty(depositPolicy.getStart())){
            depositPolicy.setStartTime(TimeUtil.stringToTimeStamp(depositPolicy.getStart(),TimeUtil.TIME_YYYY_MM_DD_HH_MM_SS));
        }
        if(!StringUtils.isEmpty(depositPolicy.getEnd())){
            depositPolicy.setEndTime(TimeUtil.stringToTimeStamp(depositPolicy.getEnd(),TimeUtil.TIME_YYYY_MM_DD_HH_MM_SS));
        }
        boolean save = save(depositPolicy);
        return depositPolicy;
    }

    @Override
    public Boolean delete(Long id) {
        return removeById(id);
    }

    @Override
    public Boolean edit(BallDepositPolicy depositPolicy) {
        return updateById(depositPolicy);
    }

    @Override
    public Boolean status(BallDepositPolicy slideshow) {
        BallDepositPolicy edit = BallDepositPolicy.builder()
                .status(slideshow.getStatus())
                .build();
        edit.setId(slideshow.getId());
        return updateById(edit);
    }
}
