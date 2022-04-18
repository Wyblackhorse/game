package com.oxo.ball.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.oxo.ball.bean.dao.BallBalanceChange;
import com.oxo.ball.bean.dao.BallPlayer;
import com.oxo.ball.bean.dto.req.player.BalanceChangeRequest;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.admin.IBallPlayerService;
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

public class BallPlayerController {

    @Resource
    IBallPlayerService ballPlayerService;
    @Resource
    IBasePlayerService iBasePlayerService;
    @Resource
    IBallBalanceChangeService ballBalanceChangeService;

    @PostMapping
    public Object index(BallPlayer query, @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        SearchResponse<BallPlayer> search = ballPlayerService.search(query, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }

    @PostMapping("add")
    public Object add(@RequestBody BallPlayer sysUserRequest){
        BallPlayer insert = new BallPlayer();
        try {
            insert = ballPlayerService.insert(sysUserRequest);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return insert.getId()!=null?BaseResponse.successWithData(insert):BaseResponse.failedWithMsg("增加失败~");
    }
    @PostMapping("edit")
    public Object editSave(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.edit(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改账号信息失败~");
    }
    @PostMapping("edit_pwd")
    public Object editPwd(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editPwd(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改账号信息失败~");
    }
    @PostMapping("edit_pay_pwd")
    public Object editPayPwd(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editPayPwd(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改账号信息失败~");
    }
    @PostMapping("status")
    public Object status(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editStatus(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改账号信息失败~");
    }
    @PostMapping("add_balance")
    public Object addBalance(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editAddBalance(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改账号信息失败~");
    }
    @PostMapping("captcha_pass")
    public Object captchaPass(@RequestBody BallPlayer ballPlayer){
        Boolean aBoolean = ballPlayerService.editCaptchaPass(ballPlayer);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改账号信息失败~");
    }
    @PostMapping("log")
    public Object balanceChange(@RequestParam("playerId") Long playerId,@RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        BallPlayer ballPlayer = BallPlayer.builder().build();
        ballPlayer.setId(playerId);
        SearchResponse<BallBalanceChange> response = ballBalanceChangeService.search(ballPlayer, new BalanceChangeRequest(), pageNo, pageSize);
        return BaseResponse.successWithData(response);
    }
}
