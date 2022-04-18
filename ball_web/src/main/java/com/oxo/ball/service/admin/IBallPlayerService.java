package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallPlayer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 * 玩家账号 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallPlayerService extends IService<BallPlayer> {

    SearchResponse<BallPlayer> search(BallPlayer query, Integer pageNo, Integer pageSize);

    BallPlayer insert(BallPlayer ballPlayer);

    Boolean edit(BallPlayer ballPlayer);
    Boolean editPwd(BallPlayer ballPlayer);
    Boolean editPayPwd(BallPlayer ballPlayer);
    Boolean editStatus(BallPlayer ballPlayer);
    Boolean editAddBalance(BallPlayer ballPlayer);
    Boolean editCaptchaPass(BallPlayer ballPlayer);
    Boolean info(BallPlayer ballPlayer);
    SearchResponse<BallBalanceChange> log(BallBalanceChange balanceChange,Integer pageNo,Integer pageSize);

}
