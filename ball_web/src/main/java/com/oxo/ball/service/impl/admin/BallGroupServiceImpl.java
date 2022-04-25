package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallGroup;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.mapper.BallGroupMapper;
import com.oxo.ball.service.admin.BallGroupService;
import com.oxo.ball.utils.RedisUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author flooming
 */
@Service
public class BallGroupServiceImpl extends ServiceImpl<BallGroupMapper, BallGroup> implements BallGroupService {
    @Resource
    BallGroupMapper ballGroupMapper;
    @Resource
    RedisUtil redisUtil;

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
        //插入新角色清除全角色缓存
        clearCache();
        return ballGroup;
    }

    @Override
    @CacheEvict(value = "ball_role", key = "'_ID_' + #id")
    public Boolean delete(Long id) {
        boolean suc = (ballGroupMapper.deleteById(id) == 1);
        if(suc){
            //删除角色清除全角色
            clearCache();
            //删除角色清除角色对应权限
            clearCache(id);
        }
        return suc;
    }

    @Override
    @CacheEvict(value = "ball_role", key = "'_ID_' + #editBallGroup.getId()")
    public Boolean edit(BallGroup editBallGroup) {
        //清除原来的授权
        ballGroupMapper.clearAuthOfRole(editBallGroup.getId());
        ballGroupMapper.addAuthOfRole(editBallGroup.getId(),editBallGroup.getAuthsId());
        ballGroupMapper.updateById(editBallGroup);
        //清除角色对应权限缓存
        clearCache(editBallGroup.getId());
        return true;
    }
    private void clearCache(){
        redisUtil.del("ball_role_all::all");
    }
    private void clearCache(Long roleId){
        //清除角色对应权限缓存
        redisUtil.del("ball_menu_by_role::_"+roleId);
    }
    @Override
    @Cacheable(value = "ball_role_all", key = "'all'", unless = "#result == null")
    public List<BallGroup> findAll() {
        QueryWrapper query = new QueryWrapper();
        query.eq("status",1);
        return list(query);
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
