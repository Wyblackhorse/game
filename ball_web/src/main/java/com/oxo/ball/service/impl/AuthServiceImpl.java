package com.oxo.ball.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.service.AuthService;
import com.oxo.ball.service.BallMenuService;
import com.oxo.ball.service.BallAdminService;
import com.oxo.ball.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author flooming
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    BallAdminService ballAdminService;

    @Autowired
    RedisUtil redisUtil;
    @Resource
    BallMenuService ballMenuService;

    public static final String REDIS_AUTH_KEY = "ball_auth_user_rec";

    @Override
    public String buildToken(BallAdmin user) {
        String result = JWT.create().withAudience(user.getId().toString(),
                String.valueOf(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(user.getPassword()));

        redisUtil.hset(REDIS_AUTH_KEY, user.getId().toString(), result);

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

        String recToken = (String)redisUtil.hget(REDIS_AUTH_KEY, userId.toString());
        if(!token.equals(recToken)) {
            return TOKEN_INVALID;
        }

        BallAdmin user = ballAdminService.findById(userId);
        if (user == null) {
            return TOKEN_INVALID;
        }

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            return TOKEN_INVALID;
        }

        //TODO 判定是否具有访问权限
        List<String> byRole = ballMenuService.findPathsByRole(user.getRoleId());
        if(byRole.contains(path)){
            return 1;
        }

        return HAVE_NO_AUTH;
    }

    @Override
    public void clearAuth(@NotNull BallAdmin user) {
        redisUtil.hdel(REDIS_AUTH_KEY, user.getId().toString());
    }

    @Override
    public void clearCurrentUser() {
    }

}
