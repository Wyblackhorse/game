package com.oxo.ball.controller.player;

import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.BallAdmin;
import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallGameLossPerCent;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.GameFinishRequest;
import com.oxo.ball.bean.dto.req.player.GameRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallGameLossPerCentService;
import com.oxo.ball.service.player.AuthPlayerService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 玩家账号 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/player/game")
@Api(tags = "玩家 - 赛事")
public class PlayerGameController {

    @Resource
    IPlayerGameService playerGameService;
    @Resource
    private IBallGameLossPerCentService gameLossPerCentService;

    @ApiOperation(
            value = "全部赛事",
            notes = "全部赛事" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime",value = "日期选项0全部,1今天,2明日/昨日",required = true),
            @ApiImplicitParam(name = "status",value = "状态选项0未结束1已开始2已结束",required = true),
            @ApiImplicitParam(name = "pageNo",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "数量")
    })
    @PostMapping
    public Object index(@Validated GameRequest query, @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallGame> search = playerGameService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }

    @ApiOperation(
            value = "赛事详情",
            notes = "赛事详情" ,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "gameId",value = "赛事ID",required = true)
    })
    @GetMapping
    public Object getGameOdds(@RequestParam("gameId") Long gameId) throws TokenInvalidedException {
        Map<String,Object> data = new HashMap<>();
        data.put("game",playerGameService.findById(gameId));
        List<BallGameLossPerCent> oddsList = gameLossPerCentService.findByGameId(gameId);
        data.put("lossPerCent",oddsList);
        return BaseResponse.successWithData(data);
    }

    @ApiOperation(
            value = "赛事结果",
            notes = "赛事结果" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime",value = "日期选项0今天,1昨天,2近七日",required = true),
            @ApiImplicitParam(name = "pageNo",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "数量")
    })
    @PostMapping("finished")
    public Object finished(@Validated GameFinishRequest query, @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallGame> search = playerGameService.searchFinish(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }

}
