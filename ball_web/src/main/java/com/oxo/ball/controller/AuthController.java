package com.oxo.ball.controller;

import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dto.req.AuthEditPwdRequest;
import com.oxo.ball.bean.dto.req.AuthLoginRequest;
import com.oxo.ball.bean.dto.resp.AuthLoginResponse;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.service.AuthService;
import com.oxo.ball.service.BallMenuService;
import com.oxo.ball.service.BallAdminService;
import com.oxo.ball.service.impl.AuthServiceImpl;
import com.oxo.ball.utils.PasswordUtil;
import com.oxo.ball.utils.RedisUtil;
import io.undertow.util.StatusCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author flooming
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    BallAdminService sysUserSer;
    @Resource
    BallMenuService ballMenuService;

    @Resource
    AuthService authService;
    @Resource
    RedisUtil redisUtil;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody AuthLoginRequest req) {
        BallAdmin ballAdmin = null;
        try {
            ballAdmin = sysUserSer.findByUsername(req.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(ballAdmin == null || !ballAdmin.getPassword().equals(PasswordUtil.genPasswordMd5(req.getPassword()))) {
            return new BaseResponse(500,"账号或密码错误!");
        }
//        if(!sysUser.getRole().equals(req.getIsAdmin())){
//            return new BaseResponse(500,"不合法的账号，请联系管理员");
//        }

        AuthLoginResponse userBean = new AuthLoginResponse();
        userBean.setId(ballAdmin.getId());
        userBean.setUserName(ballAdmin.getUsername());
//        userBean.setToken(authService.buildToken(sysUser));

        Object hget = redisUtil.hget(AuthServiceImpl.REDIS_AUTH_KEY, ballAdmin.getId().toString());
        if(hget==null || StringUtils.isEmpty(hget.toString())){
            userBean.setToken(authService.buildToken(ballAdmin));
        }else{
            userBean.setToken(hget.toString());
        }
        return new BaseResponse<>(userBean);
    }

    @GetMapping("userinfo")
    public BaseResponse getUserInfo(HttpServletRequest request){
        BallAdmin systemUser = sysUserSer.getCurrentUser(request.getHeader("token"));
        List<String> pathsByRole = ballMenuService.findPathsByRole(systemUser.getRoleId());
        return BaseResponse.successWithData(pathsByRole);
    }

    @PostMapping("/logout")
    public BaseResponse logout(HttpServletRequest request) {
        BallAdmin systemUser = sysUserSer.getCurrentUser(request.getHeader("token"));
        if(systemUser == null) {
            return new BaseResponse("未登录");
        }

//        authService.clearAuth(systemUser);

        return new BaseResponse(StatusCodes.OK, "注销成功");
    }

    @PostMapping("/editPwd")
    public BaseResponse editPwd(@RequestBody AuthEditPwdRequest req, HttpServletRequest request) {
        BallAdmin systemUser = sysUserSer.getCurrentUser(request.getHeader("token"));
        if(systemUser == null) {
            throw new RuntimeException("系统错误");
        }

        if(!systemUser.getPassword().equals(PasswordUtil.genPasswordMd5(req.getOrigin()))) {
            throw new RuntimeException("原始密码不一致");
        }

        if(!req.getConfirmed().equals(req.getNewpwd())) {
            throw new RuntimeException("确认密码不一致");
        }

        if(!sysUserSer.editPwd(systemUser.getId(), PasswordUtil.genPasswordMd5(req.getConfirmed()))) {
            throw new RuntimeException("系统错误");
        }
        redisUtil.hdel(AuthServiceImpl.REDIS_AUTH_KEY, systemUser.getId().toString());
        return new BaseResponse(StatusCodes.OK, "修改成功");
    }
}
