package com.oxo.ball.service.player;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.AuthLoginRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;

public interface IPlayerService extends IService<BallPlayer> {
    BallPlayer findByUsername(String username);
    BallPlayer getCurrentUser(String token);
    boolean editPwd(Long id, String password);

    BaseResponse registPlayer(BallPlayer ballPlayer);

    BaseResponse login(AuthLoginRequest req);
}
