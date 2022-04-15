package com.oxo.ball.service.player;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.PlayerAuthLoginRequest;
import com.oxo.ball.bean.dto.req.player.PlayerRegistRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface IPlayerService extends IService<BallPlayer> {
    BallPlayer findById(Long id);
    BallPlayer findByUsername(String username);
    BallPlayer findByInvitationCode(String invitationCode);
    BallPlayer getCurrentUser(String token);
    boolean editPwd(Long id, String password);
    BaseResponse registPlayer(PlayerRegistRequest ballPlayer, String ipAddress);
    BaseResponse login(PlayerAuthLoginRequest req);
    BaseResponse getVerifyCode() throws IOException;
    BaseResponse checkVerifyCode(String verifyKey,String code);

}
