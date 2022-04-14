package com.oxo.ball.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallGroup;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallGroupMapper;
import com.oxo.ball.service.BallGroupService;
import com.oxo.ball.utils.TimeUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author flooming
 */
@Service
public class BallGroupServiceImpl extends ServiceImpl<BallGroupMapper, BallGroup> implements BallGroupService {
    @Resource
    BallGroupMapper ballGroupMapper;

    @Override
    @Cacheable(value = "ball_role", key = "'_ID_' + #id", unless = "#result == null")
    public BallGroup findById(Long id) {
        return ballGroupMapper.selectById(id);
    }

    @Override
    @CachePut(value = "ball_role", key = "'_ID_' + #result.getId()")
    @Transactional(rollbackFor = Exception.class)
    public BallGroup insert(BallGroup ballGroup) {
        ballGroup.setCreatedAt(System.currentTimeMillis());
        ballGroup.setUpdatedAt(0L);
        ballGroupMapper.insert(ballGroup);
        //再插入角色权限数据
        Long id = ballGroup.getId();
        ballGroupMapper.addAuthOfRole(id,ballGroup.getAuthsId());
        return ballGroup;
    }

    @Override
    @CacheEvict(value = "ball_role", key = "'_ID_' + #id")
    public Boolean delete(Long id) {
        return ballGroupMapper.deleteById(id) == 1;
    }

    @Override
    public Boolean edit(BallGroup editBallGroup) {
        //清除原来的授权
        ballGroupMapper.clearAuthOfRole(editBallGroup.getId());
        ballGroupMapper.addAuthOfRole(editBallGroup.getId(),editBallGroup.getAuthsId());
        ballGroupMapper.updateById(editBallGroup);
        return true;
    }

    @Override
    public SearchResponse<BallGroup> search(BallGroup role, Integer pageNo, Integer pageSize) {
        SearchResponse<BallGroup> response = new SearchResponse<>();

        Page<BallGroup> page = new Page<>(pageNo, pageSize);
        QueryWrapper<BallGroup> query = new QueryWrapper<>();

        IPage<BallGroup> pages = ballGroupMapper.selectPage(page, query);

        response.setPageNo(pages.getCurrent());
        response.setPageSize(pages.getSize());
        response.setTotalCount(pages.getTotal());
        response.setTotalPage(pages.getPages());
        response.setResults(pages.getRecords());

        return response;
    }

}
