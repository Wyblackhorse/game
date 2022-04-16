package com.oxo.ball.controller.player;

import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.BalanceChangeRequest;
import com.oxo.ball.bean.dto.req.player.GameRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;



/**
 * <p>
 * 玩家账号 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/player")
@Api(tags = "玩家 - 个人数据")
public class PlayerController {

    @Resource
    IPlayerService playerService;
    @Resource
    AuthPlayerService authPlayerService;
    @Resource
    IBasePlayerService basePlayerService;
    @Resource
    IBallBalanceChangeService ballBalanceChangeService;
    @ApiOperation(
            value = "个人资料",
            notes = "个人资料" ,
            httpMethod = "GET")
    @GetMapping("player_info")
    public Object getPlayerInfo(HttpServletRequest request) throws TokenInvalidedException {
        BallPlayer player = playerService.getCurrentUser(request);
        return BaseResponse.successWithData(basePlayerService.findById(player.getId()));
    }

    @ApiOperation(
            value = "余额变动记录",
            notes = "余额变动记录" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "1充值 2提现 3投注 4赢 5佣金 6人工"),
            @ApiImplicitParam(name = "pageNo",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "数量")
    })
    @PostMapping("balance_change")
    public Object index(BalanceChangeRequest balanceChangeRequest,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        HttpServletRequest request) throws TokenInvalidedException {
        BallPlayer currentUser = playerService.getCurrentUser(request);
        SearchResponse<BallBalanceChange> search = ballBalanceChangeService.search(currentUser,balanceChangeRequest, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
}
