package com.oxo.ball.controller.player;

import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(
            value = "个人资料",
            notes = "个人资料" ,
            httpMethod = "GET")
    @GetMapping("player_info")
    public Object getPlayerInfo(HttpServletRequest request){
        BallPlayer player = playerService.getCurrentUser(request.getHeader("token"));
        return BaseResponse.successWithData(playerService.findById(player.getId()));
    }

}
