package com.oxo.ball.service.impl.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oxo.ball.bean.dao.BallMenu;
import com.oxo.ball.mapper.BallMenuMapper;
import com.oxo.ball.service.admin.BallMenuService;
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
    @Cacheable(value = "ball_menu", key = "'_ID_' + #id", unless = "#result == null")
    public BallMenu findById(Long id) {
        return ballMenuMapper.selectById(id);
    }

    @Override
    @Cacheable(value = "ball_menu_by_role",key="'_'+#roleId",unless ="#result==null")
    public List<BallMenu> findByRole(Long roleId) {
        //通过角色ID查找权限
        //修改角色后清除本缓存
        List<BallMenu> sysUserDAO = ballMenuMapper.findByRole(roleId);
        return sysUserDAO;
    }

    @Override
    public List<BallMenu> findAll() {
        QueryWrapper<BallMenu> query = new QueryWrapper<>();
        query.orderByAsc("is_menu");
        List<BallMenu> authList = ballMenuMapper.selectList(query);
        return authList;
    }



}
