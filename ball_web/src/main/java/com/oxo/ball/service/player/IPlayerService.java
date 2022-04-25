package com.oxo.ball.service.player;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.PlayerAuthLoginRequest;
import com.oxo.ball.bean.dto.req.player.PlayerRegistRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface IPlayerService extends IService<BallPlayer> {

    BallPlayer getCurrentUser(HttpServletRequest request) throws TokenInvalidedException;
    boolean editPwd(Long id, String password);
    BaseResponse registPlayer(PlayerRegistRequest ballPlayer, String ipAddress);
    BaseResponse login(PlayerAuthLoginRequest req, HttpServletRequest request);
    BaseResponse getVerifyCode() throws IOException;
    BaseResponse checkVerifyCode(String verifyKey,String code);

    BaseResponse recharge(BallPlayer currentUser, Long money);

    BaseResponse withdrawal(BallPlayer currentUser, Long money);
}
