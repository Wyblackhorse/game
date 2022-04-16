package com.oxo.ball.controller.player;

import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dao.BallSystemConfig;
import com.oxo.ball.bean.dto.req.AuthEditPwdRequest;
import com.oxo.ball.bean.dto.req.player.PlayerAuthLoginRequest;
import com.oxo.ball.bean.dto.req.player.PlayerRegistRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.oxo.ball.service.impl.player.PlayerAuthServiceImpl;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.IpUtil;
import com.oxo.ball.utils.MapUtil;
import com.oxo.ball.utils.PasswordUtil;
import com.oxo.ball.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.undertow.util.StatusCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author flooming
 */
@Api(tags = "玩家 - 登录/注册")
@RestController
@RequestMapping("/player/auth")
public class PlayerAuthController {
    @Autowired
    IPlayerService playerService;
    @Resource
    RedisUtil redisUtil;
    @Resource
    IBallSystemConfigService systemConfigService;

    @ApiOperation(
            value = "登录认证",
            notes = "登录认证" +
                    "<br>响应code" +
                    "<br>101-密码输入错误计数"+
                    "<br>102-密码输入错误上限",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "账号",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true),
            @ApiImplicitParam(name = "code",value = "验证码",required = true),
            @ApiImplicitParam(name = "verifyKey",value = "获取验证码时的key",required = true)
    })
    @PostMapping("/login")
    public BaseResponse login(@Validated PlayerAuthLoginRequest req) {
        return playerService.login(req);
    }

    /**
     *
     * @param ballPlayer
     * @return
     */
    @ApiOperation(
            value = "注册",
            notes = "注册",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "账号",required = true),
            @ApiImplicitParam(name = "password",value = "密码",required = true),
            @ApiImplicitParam(name = "twoPassword",value = "再次密码",required = true),
            @ApiImplicitParam(name = "invitationCode",value = "邀请码",required = false),
            @ApiImplicitParam(name = "verifyKey",value = "获取验证码时的key",required = true),
            @ApiImplicitParam(name = "code",value = "验证码",required = true)
    })
    @PostMapping("/regist")
    public BaseResponse regist(@Validated PlayerRegistRequest ballPlayer,HttpServletRequest request) {
        BaseResponse response = playerService.registPlayer(ballPlayer,IpUtil.getIpAddress(request));
        return response;
    }

    /**
     * 获取验证码方法
     */
    @ApiOperation(
            value = "获取验证码",
            notes = "获取验证码",
            httpMethod = "GET")
    @ApiImplicitParams({
    })
    @GetMapping("/verify_code")
    public BaseResponse getVerifyCode() throws IOException {
        return playerService.getVerifyCode();
    }
    /**
     * 获取验证码方法
     */
    @ApiOperation(
            value = "注册获取系统配置",
            notes = "注册获取系统配置",
            httpMethod = "GET")
    @ApiImplicitParams({
    })
    @GetMapping("/sys_config")
    public BaseResponse getSystemConfig() {
        BallSystemConfig systemConfig = systemConfigService.getSystemConfig();
        Map<String, Object> invitation_code = MapUtil.newMap("invitation_code", systemConfig.getRegisterIfNeedVerificationCode());
        invitation_code.put("info","1.需要2.不需要");
        return BaseResponse.successWithData(invitation_code);
    }
    @ApiOperation(
            value = "判定验证码",
            notes = "判定验证码",
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "verifyKey",value = "获取验证码时拿到的key",required = true),
    })
    @GetMapping("/verify_code_check")
    public BaseResponse checkVerifyCode(@RequestParam("verifyKey") String verifyKey,@RequestParam("code") String code) {
        return playerService.checkVerifyCode(verifyKey,code);
    }

    @ApiOperation(
            value = "登出",
            notes = "登出",
            httpMethod = "GET")
    @ApiImplicitParams({
    })
    @GetMapping("/logout")
    public BaseResponse logout(HttpServletRequest request) throws TokenInvalidedException {
        BallPlayer systemUser = playerService.getCurrentUser(request);
        if(systemUser == null) {
            return new BaseResponse("未登录");
        }
        return new BaseResponse(StatusCodes.OK, "注销成功");
    }

    @ApiOperation(
            value = "修改密码",
            notes = "修改密码",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "origin",value = "原密码",required = true),
            @ApiImplicitParam(name = "newpwd",value = "新密码",required = true),
            @ApiImplicitParam(name = "confirmed",value = "再次密码",required = true),
    })
    @PostMapping("/editPwd")
    public BaseResponse editPwd(AuthEditPwdRequest req, HttpServletRequest request) throws TokenInvalidedException {
        BallPlayer player = playerService.getCurrentUser(request);
        if(player == null) {
            throw new RuntimeException("系统错误");
        }

        if(!player.getPassword().equals(PasswordUtil.genPasswordMd5(req.getOrigin()))) {
            throw new RuntimeException("原始密码不一致");
        }

        if(!req.getConfirmed().equals(req.getNewpwd())) {
            throw new RuntimeException("确认密码不一致");
        }

        if(!playerService.editPwd(player.getId(), PasswordUtil.genPasswordMd5(req.getConfirmed()))) {
            throw new RuntimeException("系统错误");
        }
        redisUtil.hdel(PlayerAuthServiceImpl.REDIS_PLAYER_AUTH_KEY, player.getId().toString());
        return new BaseResponse(StatusCodes.OK, "修改成功");
    }


}
