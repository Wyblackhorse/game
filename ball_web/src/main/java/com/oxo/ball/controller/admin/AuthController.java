package com.oxo.ball.controller.admin;

import com.google.zxing.WriterException;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallMenu;
import com.oxo.ball.bean.dto.req.AuthEditPwdRequest;
import com.oxo.ball.bean.dto.req.AuthLoginRequest;
import com.oxo.ball.bean.dto.req.SysUserEditRequest;
import com.oxo.ball.bean.dto.resp.AuthLoginResponse;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.interceptor.MainOper;
import com.oxo.ball.interceptor.SubOper;
import com.oxo.ball.service.admin.AuthService;
import com.oxo.ball.service.admin.BallMenuService;
import com.oxo.ball.service.admin.BallAdminService;
import com.oxo.ball.service.impl.admin.AuthServiceImpl;
import com.oxo.ball.utils.GoogleAuthenticationTool;
import com.oxo.ball.utils.PasswordUtil;
import com.oxo.ball.utils.RedisUtil;
import io.undertow.util.StatusCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author flooming
 */
@RestController
@RequestMapping("/auth")
@MainOper("个人设置")
public class AuthController {
    @Autowired
    BallAdminService ballAdminService;
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
            ballAdmin = ballAdminService.findByUsername(req.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ballAdmin == null || !ballAdmin.getPassword().equals(PasswordUtil.genPasswordMd5(req.getPassword()))) {
            return new BaseResponse(500, "账号或密码错误!");
        }
//        if(!sysUser.getRole().equals(req.getIsAdmin())){
//            return new BaseResponse(500,"不合法的账号，请联系管理员");
//        }

        AuthLoginResponse userBean = new AuthLoginResponse();
        userBean.setId(ballAdmin.getId());
        userBean.setUserName(ballAdmin.getUsername());
//        userBean.setToken(authService.buildToken(sysUser));
        if (StringUtils.isEmpty(ballAdmin.getGoogleCode())) {
            //没有绑定google验证
            String generateSecretKey = GoogleAuthenticationTool.generateSecretKey();
            String qrCodeString = GoogleAuthenticationTool.spawnScanQRString(ballAdmin.getUsername(), generateSecretKey, "oxo_ball");
            String qrCodeImageBase64 = null;
            try {
                qrCodeImageBase64 = GoogleAuthenticationTool.createQRCode(qrCodeString, null, 128, 128);
            } catch (WriterException | IOException e) {
                e.printStackTrace();
            }
            userBean.setGtokenKey(generateSecretKey);
            userBean.setGtokenQr(qrCodeImageBase64);
            return BaseResponse.successWithData(userBean);
        }
        Object hget = redisUtil.hget(AuthServiceImpl.REDIS_AUTH_KEY, ballAdmin.getId().toString());
        if (hget == null || StringUtils.isEmpty(hget.toString())) {
            userBean.setToken(authService.buildToken(ballAdmin));
        } else {
            userBean.setToken(hget.toString());
        }
        String rightCode = GoogleAuthenticationTool.getTOTPCode(ballAdmin.getGoogleCode());
        if (!rightCode.equals(req.getGoogleCode())) {
            return BaseResponse.failedWithMsg("google验证码输入错误~");
        }
        userBean.setGtokenKey("ok");
        //验证GOOGLE验证码
        return new BaseResponse<>(userBean);
    }

    @PostMapping("userinfo")
    public BaseResponse bindGoogleValid(@RequestBody AuthLoginRequest loginRequest) {
        BallAdmin ballAdmin = ballAdminService.findByUsername(loginRequest.getUsername());
        if (!StringUtils.isEmpty(ballAdmin.getGoogleCode())) {
            return BaseResponse.failedWithMsg("已经绑定了google验证码，不需要重复绑定~");
        }
        String rightCode = GoogleAuthenticationTool.getTOTPCode(loginRequest.getGoogleKey());
        System.out.println(rightCode);
        if (!rightCode.equals(loginRequest.getGoogleCode())) {
            return BaseResponse.failedWithMsg("google验证码输入错误~");
        }
        //验证码正确
        SysUserEditRequest edit = new SysUserEditRequest();
        edit.setId(ballAdmin.getId());
        edit.setGoogleCode(loginRequest.getGoogleKey());
        ballAdminService.edit(edit);
        AuthLoginResponse userBean = new AuthLoginResponse();
        userBean.setId(ballAdmin.getId());
        userBean.setUserName(ballAdmin.getUsername());
        Object hget = redisUtil.hget(AuthServiceImpl.REDIS_AUTH_KEY, ballAdmin.getId().toString());
        if (hget == null || StringUtils.isEmpty(hget.toString())) {
            userBean.setToken(authService.buildToken(ballAdmin));
        } else {
            userBean.setToken(hget.toString());
        }
        return new BaseResponse<>(userBean);
    }

    @GetMapping("userinfo")
    public BaseResponse getUserInfo(HttpServletRequest request) {
        BallAdmin systemUser = ballAdminService.getCurrentUser(request.getHeader("token"));
        List<BallMenu> byRole = ballMenuService.findByRole(systemUser.getRoleId());
        List<String> auths = new ArrayList<>();
        for (BallMenu auth : byRole) {
            auths.add(auth.getPath());
        }
        Map<String,Object> data = new HashMap<>();
        data.put("auths",auths);
        data.put("name",systemUser.getUsername());
        return BaseResponse.successWithData(data);
    }

    @PostMapping("/logout")
    public BaseResponse logout(HttpServletRequest request) {
        BallAdmin systemUser = ballAdminService.getCurrentUser(request.getHeader("token"));
        if (systemUser == null) {
            return new BaseResponse("未登录");
        }

//        authService.clearAuth(systemUser);

        return new BaseResponse(StatusCodes.OK, "注销成功");
    }

    @PostMapping("/editPwd")
    @SubOper("修改密码")
    public BaseResponse editPwd(@RequestBody AuthEditPwdRequest req, HttpServletRequest request) {
        BallAdmin systemUser = ballAdminService.getCurrentUser(request.getHeader("token"));
        if (systemUser == null) {
            throw new RuntimeException("系统错误");
        }

        if (!systemUser.getPassword().equals(PasswordUtil.genPasswordMd5(req.getOrigin()))) {
            throw new RuntimeException("原始密码不一致");
        }

        if (!req.getConfirmed().equals(req.getNewpwd())) {
            throw new RuntimeException("确认密码不一致");
        }

        if (!ballAdminService.editPwd(systemUser.getId(), PasswordUtil.genPasswordMd5(req.getConfirmed()))) {
            throw new RuntimeException("系统错误");
        }
        redisUtil.hdel(AuthServiceImpl.REDIS_AUTH_KEY, systemUser.getId().toString());
        return new BaseResponse(StatusCodes.OK, "修改成功");
    }
}
