package com.oxo.ball.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.BalanceChangeRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.interceptor.MainOper;
import com.oxo.ball.interceptor.SubOper;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.admin.IBallPlayerService;
import com.oxo.ball.service.player.IPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 玩家账号 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/player")
@MainOper("会员管理")
public class BallPlayerController {

    @Resource
    IBallPlayerService ballPlayerService;
    @Resource
    IBasePlayerService iBasePlayerService;
    @Resource
    IBallBalanceChangeService ballBalanceChangeService;
    @Autowired
    IPlayerService playerService;

    @PostMapping
    public Object index(BallPlayer query, @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallPlayer> search = ballPlayerService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }

    @PostMapping("add")
    public Object add(@RequestBody BallPlayer sysUserRequest){
        Object insert = ballPlayerService.insert(sysUserRequest);
        return insert;
    }
    @PostMapping("edit")
    public Object editSave(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.edit(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
    @PostMapping("edit_pwd")
    public Object editPwd(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editPwd(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
    @PostMapping("edit_pay_pwd")
    public Object editPayPwd(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editPayPwd(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
    @PostMapping("status")
    public Object status(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editStatus(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
    @PostMapping("add_balance")
    @SubOper("上分")
    public BaseResponse<BallPlayer> addBalance(@RequestBody BallPlayer ballPlayer){
        BaseResponse<BallPlayer> response = ballPlayerService.editAddBalance(ballPlayer);
        if(response.getCode().equals(BaseResponse.SUCCESS.getCode())){
            response.setRemark("会员["+response.getData().getUsername()+"]上分["+ballPlayer.getBalance()+"]");
        }
        return response;
    }
    @PostMapping("captcha_pass")
    public Object captchaPass(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editCaptchaPass(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("error");
    }
    @PostMapping("log")
    public Object balanceChange(@RequestParam("playerId") Long playerId,@RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        BallPlayer ballPlayer = BallPlayer.builder().build();
        ballPlayer.setId(playerId);
        SearchResponse<BallBalanceChange> response = ballBalanceChangeService.search(ballPlayer, new BalanceChangeRequest(), pageNo, pageSize);
        return BaseResponse.successWithData(response);
    }
    @PostMapping("info")
    public Object balanceChange(@RequestParam("playerId") Long playerId){
        BallPlayer ballPlayer = BallPlayer.builder().build();
        ballPlayer.setId(playerId);
        return ballPlayerService.info(ballPlayer);
    }
}
