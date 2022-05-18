package com.oxo.ball.service.player;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.auth.PlayerDisabledException;
import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.DataCenterRequest;
import com.oxo.ball.bean.dto.req.player.PlayerAuthLoginRequest;
import com.oxo.ball.bean.dto.req.player.PlayerRegistRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IPlayerService extends IService<BallPlayer> {

    int  STATUS_DISABLED = 403;
    String REDIS_DATA_CENTER = "ball_player_data_center";
    String REDIS_DATA_CENTER_DETAIL = "ball_player_data_center_detail";

    BallPlayer getCurrentUser(HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException;
    BaseResponse registPlayer(PlayerRegistRequest ballPlayer, String ipAddress);
    BaseResponse login(PlayerAuthLoginRequest req, HttpServletRequest request);
    BaseResponse getVerifyCode() throws IOException;
    BaseResponse checkVerifyCode(String verifyKey,String code);

    BaseResponse recharge(BallPlayer currentUser, Long money);

    BaseResponse withdrawal(BallPlayer currentUser, Long money);

    Map<String,Object> dataCenter(BallPlayer player, DataCenterRequest dataCenterRequest);

    List<Map<String,Object>> dataCenterDetail(BallPlayer player, DataCenterRequest dataCenterRequest);
}
