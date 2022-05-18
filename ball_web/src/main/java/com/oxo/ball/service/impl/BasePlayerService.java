package com.oxo.ball.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.mapper.BallPlayerMapper;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.utils.RedisUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BasePlayerService extends ServiceImpl<BallPlayerMapper, BallPlayer> implements IBasePlayerService {
    @Resource
    private RedisUtil redisUtil;

    @Override
    @Cacheable(value = "ball_player_by_id", key = "#id", unless = "#result == null")
    public BallPlayer findById(Long id) {
        return getById(id);
    }
    @Override
    public BallPlayer findByIdNoCache(Long id) {
        return getById(id);
    }

    @Cacheable(value = "ball_player_by_username", key = "#username", unless = "#result == null")
    @Override
    public BallPlayer findByUsername(String username) {
        QueryWrapper query = new QueryWrapper();
        query.eq("username", username);
        BallPlayer ballPlayer = getOne(query);
        return ballPlayer;
    }

    @Override
    @Cacheable(value = "ball_player_by_invitation_code", key = "#invitationCode", unless = "#result == null")
    public BallPlayer findByInvitationCode(String invitationCode) {
        QueryWrapper query = new QueryWrapper();
        query.eq("invitation_code", invitationCode);
        BallPlayer ballPlayer = getOne(query);
        return ballPlayer;
    }

    @Override
    public boolean editAndClearCache(BallPlayer edit,BallPlayer db) {
        try {
            return updateById(edit);
        }catch (Exception ex){
            return false;
        }finally {
            //清除账号缓存
            redisUtil.del("ball_player_by_id::"+db.getId());
            redisUtil.del("ball_player_by_username::"+db.getUsername());
            redisUtil.del("ball_player_by_invitation_code::"+db.getInvitationCode());
        }
    }

    @Override
    public void editMultGroupNum(String treeIds,int quantity) {
        baseMapper.updateTreeGroupNum(treeIds,quantity);
    }
}
