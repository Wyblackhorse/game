package com.oxo.ball.controller.player;

import com.oxo.ball.auth.PlayerDisabledException;
import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallGameLossPerCent;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.BetPreRequest;
import com.oxo.ball.bean.dto.req.player.BetRequest;
import com.oxo.ball.bean.dto.req.player.GameRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import com.oxo.ball.service.player.IPlayerBetService;
import com.oxo.ball.service.player.IPlayerGameService;
import com.oxo.ball.service.player.IPlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;


/**
 * <p>
 * 玩家账号 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/player/bet")
@Api(tags = "玩家 - 赛事下注")
public class PlayerBetController {

    @Resource
    IPlayerBetService betService;
    @Resource
    IPlayerService playerService;
    @Resource
    IBallGameLossPerCentService gameLossPerCentService;
    @ApiOperation(
            value = "下注",
            notes = "下注" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId",value = "游戏ID",required = true),
            @ApiImplicitParam(name = "oddsId",value = "赔率ID",required = true),
            @ApiImplicitParam(name = "money",value = "下注金额",required = true)
    })
    @PostMapping
    public Object bets(@Validated BetRequest betRequest, HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        try {
            BallPlayer currentPlayer = playerService.getCurrentUser(request);
            BaseResponse baseResponse = betService.gameBet(betRequest,currentPlayer);
            return baseResponse;
        } catch (SQLException e) {
        }
        return BaseResponse.failedWithMsg("下注失败~");
    }

    @ApiOperation(
            value = "下注准备",
            notes = "下注准备" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId",value = "游戏ID",required = true),
            @ApiImplicitParam(name = "oddsId",value = "赔率ID",required = true)
    })
    @PostMapping("pre")
    public Object betsPrepare(@Validated BetPreRequest betRequest, HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer currentPlayer = playerService.getCurrentUser(request);
        BaseResponse baseResponse = betService.gameBetPrepare(betRequest,currentPlayer);
        return baseResponse;
    }


}
