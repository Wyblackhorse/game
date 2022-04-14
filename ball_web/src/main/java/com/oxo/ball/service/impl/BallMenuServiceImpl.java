package com.oxo.ball.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oxo.ball.bean.dao.BallMenu;
import com.oxo.ball.mapper.BallMenuMapper;
import com.oxo.ball.service.BallMenuService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author flooming
 */
@Service
public class BallMenuServiceImpl implements BallMenuService {
    @Resource
    BallMenuMapper ballMenuMapper;

    @Override
    @Cacheable(value = "sys_auth", key = "'_ID_' + #id", unless = "#result == null")
    public BallMenu findById(Long id) {
        return ballMenuMapper.selectById(id);
    }

    @Override
    @Cacheable(value = "sys_auth_by_role",key="'_'+#roleId",unless ="#result==null")
    public List<BallMenu> findByRole(Long roleId) {
        List<BallMenu> sysUserDAO = ballMenuMapper.findByRole(roleId);
        return sysUserDAO;
    }

    @Override
    public List<String> findPathsByRole(Long roleId) {
        List<BallMenu> byRole = findByRole(roleId);
        List<String> auths = new ArrayList<>();
        for(BallMenu auth:byRole){
            auths.add(auth.getPath());
        }
        return auths;
    }

    @Override
    public List<Long> findAuthIdByRole(Long roleId) {
        List<BallMenu> byRole = findByRole(roleId);
        List<Long> auths = new ArrayList<>();
        for(BallMenu auth:byRole){
            auths.add(auth.getId());
        }
        return auths;
    }

    @Override
    public List<BallMenu> findAll() {
        List<BallMenu> authList = ballMenuMapper.selectList(new QueryWrapper<>());
        //
        return authList;
    }

}
