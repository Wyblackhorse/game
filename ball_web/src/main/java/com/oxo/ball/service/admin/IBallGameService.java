package com.oxo.ball.service.admin;

import com.oxo.ball.bean.dao.BallGame;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;

/**
 * <p>
 * 游戏赛事 服务类
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
public interface IBallGameService extends IService<BallGame> {

    SearchResponse<BallGame> search(BallGame query, Integer pageNo, Integer pageSize);
    Boolean insert(BallGame ballGame);
    BaseResponse edit(BallGame ballGame);
    void whenGameStart();

    Boolean editStatus(BallGame ballGame);
    Boolean editStatusTop(BallGame ballGame);
    Boolean editStatusHot(BallGame ballGame);
    Boolean editStatusEven(BallGame ballGame);

    BaseResponse recount(BallGame ballGame);

    BaseResponse rollback(BallGame ballGame);
}
