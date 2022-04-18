package com.oxo.ball.service.impl.player;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.service.admin.IBallPlayerService;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author flooming
 */
@Service()
public class PlayerAuthServiceImpl implements AuthPlayerService {
    @Autowired
    RedisUtil redisUtil;
    @Resource
    IBasePlayerService basePlayerService;

    public static final String REDIS_PLAYER_AUTH_KEY = "ball_auth_player_user_rec";

    @Override
    public String buildToken(BallPlayer user) {
        String result = JWT.create().withAudience(user.getId().toString(),
                String.valueOf(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(user.getPassword()));

        redisUtil.hset(REDIS_PLAYER_AUTH_KEY, user.getId().toString(), result);

        return result;
    }

    @Override
    public int checkAuth(String token, String path) {
        if (token == null) {
            return TOKEN_INVALID;
        }

        Long userId;

        try {
            List<String> audience = JWT.decode(token).getAudience();
            userId = Long.parseLong(audience.get(0));
        } catch (JWTDecodeException j) {
            throw new RuntimeException("内部错误");
        }

        String recToken = (String)redisUtil.hget(REDIS_PLAYER_AUTH_KEY, userId.toString());
        if(!token.equals(recToken)) {
            return TOKEN_INVALID;
        }

        BallPlayer user = basePlayerService.findById(userId);
        if (user == null) {
            return TOKEN_INVALID;
        }

        if(user.getStatus()==PLAYER_INVALID){
            return PLAYER_INVALID;
        }

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
            return 1;
        } catch (JWTVerificationException e) {
            return TOKEN_INVALID;
        }
    }

    @Override
    public void clearAuth(@NotNull BallPlayer user) {
        redisUtil.hdel(REDIS_PLAYER_AUTH_KEY, user.getId().toString());
    }

    @Override
    public void clearCurrentUser() {
    }

}
