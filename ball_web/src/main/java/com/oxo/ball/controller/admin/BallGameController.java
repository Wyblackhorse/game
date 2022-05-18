package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallGame;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.admin.IBallGameService;
import com.oxo.ball.service.player.IPlayerGameService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 游戏赛事 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/game")
public class BallGameController {
    @Resource
    IBallGameService gameService;
    @Resource
    IPlayerGameService playerGameService;
    @PostMapping
    public Object index(BallGame query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallGame> search = gameService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
    @PostMapping("info")
    public Object info(@RequestBody BallGame ballGame){
        BallGame res = playerGameService.findById(ballGame.getId());
        return BaseResponse.successWithData(res);
    }
    @PostMapping("edit")
    public Object editSave(@RequestBody BallGame ballGame){
        BaseResponse res = gameService.edit(ballGame);
        return res;
    }
    @PostMapping("status")
    public Object status(@RequestBody BallGame ballGame){
        Boolean aBoolean = gameService.editStatus(ballGame);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
    @PostMapping("top")
    public Object top(@RequestBody BallGame ballGame){
        Boolean aBoolean = gameService.editStatusTop(ballGame);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
    @PostMapping("hot")
    public Object hot(@RequestBody BallGame ballGame){
        Boolean aBoolean = gameService.editStatusHot(ballGame);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
    @PostMapping("even")
    public Object even(@RequestBody BallGame ballGame){
        Boolean aBoolean = gameService.editStatusEven(ballGame);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
}
