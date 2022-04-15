package com.oxo.ball.controller.player;

import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.AuthEditPwdRequest;
import com.oxo.ball.bean.dto.req.AuthLoginRequest;
import com.oxo.ball.bean.dto.resp.AuthLoginResponse;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.service.impl.player.PlayerAuthServiceImpl;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.PasswordUtil;
import com.oxo.ball.utils.RedisUtil;
import com.oxo.ball.utils.VerifyCodeUtils;
import io.undertow.util.StatusCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author flooming
 */
@RestController
@RequestMapping("/player/auth")
public class PlayerAuthController {
    @Autowired
    IPlayerService playerService;
    @Resource
    RedisUtil redisUtil;

    @PostMapping("/login")
    public BaseResponse login(@RequestBody AuthLoginRequest req) {
        return playerService.login(req);
    }


    @PostMapping("/regist")
    public BaseResponse regist( BallPlayer ballPlayer) {
        BaseResponse response = playerService.registPlayer(ballPlayer);
        return response;
    }

    /**
     * 获取验证码方法
     */
    @GetMapping("/verify_code")
    public void getVerifyCode(@RequestParam("verifyKey") String verifyKey,HttpServletResponse response) throws IOException {
        playerService.getVerifyCode(verifyKey,response);
    }
    @GetMapping("/verify_code_check")
    public BaseResponse checkVerifyCode(@RequestParam("verifyKey") String verifyKey,@RequestParam("code") String code) {
        return playerService.checkVerifyCode(verifyKey,code);
    }

    @PostMapping("/logout")
    public BaseResponse logout(HttpServletRequest request) {
        BallPlayer systemUser = playerService.getCurrentUser(request.getHeader("token"));
        if(systemUser == null) {
            return new BaseResponse("未登录");
        }
        return new BaseResponse(StatusCodes.OK, "注销成功");
    }

    @PostMapping("/editPwd")
    public BaseResponse editPwd(@RequestBody AuthEditPwdRequest req, HttpServletRequest request) {
        BallPlayer player = playerService.getCurrentUser(request.getHeader("token"));
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
