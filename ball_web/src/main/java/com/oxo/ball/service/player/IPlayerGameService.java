package com.oxo.ball.service.player;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.GameFinishRequest;
import com.oxo.ball.bean.dto.req.player.GameRequest;
import com.oxo.ball.bean.dto.req.player.PlayerAuthLoginRequest;
import com.oxo.ball.bean.dto.req.player.PlayerRegistRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;

import java.io.IOException;
import java.util.List;

public interface IPlayerGameService extends IService<BallGame> {
    BallGame findById(Long id);

    SearchResponse<BallGame> search(GameRequest query, Integer pageNo, Integer pageSize);

    SearchResponse<BallGame> searchFinish(GameFinishRequest query, Integer pageNo, Integer pageSize);

    List<BallGame> findUnfinish();
}
