package com.oxo.ball.service.impl.player;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.AuthLoginRequest;
import com.oxo.ball.bean.dto.resp.AuthLoginResponse;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.mapper.BallPlayerMapper;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.PasswordUtil;
import com.oxo.ball.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 玩家账号 服务实现类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Service
public class PlayerServiceImpl extends ServiceImpl<BallPlayerMapper, BallPlayer> implements IPlayerService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    AuthPlayerService authService;

    @Override
    public BallPlayer findByUsername(String username) {
        QueryWrapper query = new QueryWrapper();
        query.eq("username",username);
        BallPlayer ballPlayer = getOne(query);
        return ballPlayer;
    }

    @Override
    public BallPlayer getCurrentUser(String token) {
        Long userId;
        try {
            List<String> audience = JWT.decode(token).getAudience();
            userId = Long.parseLong(audience.get(0));
            BallPlayer byId = getById(userId);
            return byId;
        } catch (JWTDecodeException j) {
            throw new RuntimeException("内部错误");
        } catch (Exception ex){
            return null;
        }
    }

    @Override
    public boolean editPwd(Long id, String password) {
        UpdateWrapper<BallPlayer> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        BallPlayer dao = new BallPlayer();
        dao.setPassword(password);
        return update(dao, wrapper);
    }

    @Override
    public BaseResponse registPlayer(BallPlayer ballPlayer) {
        try {
            ballPlayer = findByUsername(ballPlayer.getUsername());
            return BaseResponse.failedWithMsg(2,"账号已存在~");
        } catch (Exception e) {
            e.printStackTrace();
        }
        BallPlayer save = BallPlayer.builder()
                .username(ballPlayer.getUsername())
                .password(PasswordUtil.genPasswordMd5(ballPlayer.getPassword()))
                .build();

        boolean res = save(save);
        return res?BaseResponse.successWithMsg("注册成功~"):BaseResponse.failedWithMsg("注册失败~");
    }

    @Override
    public BaseResponse login(AuthLoginRequest req) {
        BallPlayer ballPlayer = null;
        try {
            ballPlayer = findByUsername(req.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ballPlayer == null || !ballPlayer.getPassword().equals(PasswordUtil.genPasswordMd5(req.getPassword()))) {
            return new BaseResponse(500,"账号或密码错误!");
        }

        AuthLoginResponse userBean = new AuthLoginResponse();
        userBean.setId(ballPlayer.getId());
        userBean.setUserName(ballPlayer.getUsername());

        Object hget = redisUtil.hget(PlayerAuthServiceImpl.REDIS_PLAYER_AUTH_KEY, ballPlayer.getId().toString());
        if(hget==null || StringUtils.isEmpty(hget.toString())){
            userBean.setToken(authService.buildToken(ballPlayer));
        }else{
            userBean.setToken(hget.toString());
        }
        return new BaseResponse<>(userBean);
    }
}
