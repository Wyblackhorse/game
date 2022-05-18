package com.oxo.ball.service.impl.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallMenu;
import com.oxo.ball.service.admin.AuthService;
import com.oxo.ball.service.admin.BallMenuService;
import com.oxo.ball.service.admin.BallAdminService;
import com.oxo.ball.utils.RedisUtil;
import com.oxo.ball.utils.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author flooming
 */
@Service()
public class AuthServiceImpl implements AuthService {
    @Autowired
    BallAdminService ballAdminService;

    @Autowired
    RedisUtil redisUtil;
    @Resource
    BallMenuService ballMenuService;

    public static final String REDIS_AUTH_KEY = "ball_auth_user_rec::";

    @Override
    public String buildToken(BallAdmin user) {
        String result = JWT.create().withAudience(user.getId().toString(),
                String.valueOf(System.currentTimeMillis()))
                .sign(Algorithm.HMAC256(user.getPassword()));
        //TODO 账号登录过期为8小时
        redisUtil.set(REDIS_AUTH_KEY+user.getId().toString(), result,TimeUtil.TIME_EIGHT_HOURS);

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
            throw new RuntimeException("internal error");
        }

        Object recToken = redisUtil.get(REDIS_AUTH_KEY+userId);
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
        List<BallMenu> byRole = ballMenuService.findByRole(user.getRoleId());
        List<String> authPath = new ArrayList<>();
        for(BallMenu ballMenu:byRole){
            authPath.add(ballMenu.getPath());
        }
        if(authPath.contains(path)){
            return 1;
        }

        return HAVE_NO_AUTH;
    }

    @Override
    public void clearAuth(@NotNull BallAdmin user) {
        redisUtil.del(REDIS_AUTH_KEY+user.getId());
    }

    @Override
    public void clearCurrentUser() {
    }

}
