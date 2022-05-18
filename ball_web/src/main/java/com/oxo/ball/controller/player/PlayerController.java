package com.oxo.ball.controller.player;

import com.oxo.ball.auth.PlayerDisabledException;
import com.oxo.ball.auth.TokenInvalidedException;
import com.oxo.ball.bean.dao.*;
import com.oxo.ball.bean.dto.req.AuthEditPwdRequest;
import com.oxo.ball.bean.dto.req.player.*;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.bean.dto.resp.SearchResponse;
import com.oxo.ball.service.IBasePlayerService;
import com.oxo.ball.service.admin.IBallBalanceChangeService;
import com.oxo.ball.service.admin.IBallLoggerBackService;
import com.oxo.ball.service.player.AuthPlayerService;
import com.oxo.ball.service.player.IPlayerBetService;
import com.oxo.ball.service.player.IPlayerService;
import com.oxo.ball.utils.PasswordUtil;
import com.oxo.ball.utils.ResponseMessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.undertow.util.StatusCodes;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
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
    @Resource
    IPlayerBetService betService;
    @Autowired
    IBallLoggerBackService loggerBackService;
    @ApiOperation(
            value = "个人资料",
            notes = "个人资料" ,
            httpMethod = "GET")
    @GetMapping("player_info")
    public Object getPlayerInfo(HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer player = playerService.getCurrentUser(request);
        return BaseResponse.successWithData(basePlayerService.findById(player.getId()));
    }

    @ApiOperation(
            value = "余额变动记录",
            notes = "余额变动记录" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",value = "1充值 2提现 3投注 4赢 5佣金 6人工 7撤单"),
            @ApiImplicitParam(name = "typeb",value = "1.收入,2.支出"),
            @ApiImplicitParam(name = "time",value = "1.今日,2.昨日,3.近7日"),
            @ApiImplicitParam(name = "pageNo",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "数量")
    })
    @PostMapping("balance_change")
    public Object index(BalanceChangeRequest balanceChangeRequest,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer currentUser = playerService.getCurrentUser(request);
        SearchResponse<BallBalanceChange> search = ballBalanceChangeService.search(currentUser,balanceChangeRequest, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }

    @ApiOperation(
            value = "订单中心",
            notes = "订单中心" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "1今天，2昨天，3近7日，4近10日，5近30日",required = false),
            @ApiImplicitParam(name = "type",value = "1查全部 2查反波",required = false),
            @ApiImplicitParam(name = "pageNo",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "数量")
    })
    @PostMapping("bets")
    public Object bets(@Validated PlayerBetRequest query,
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer currentUser = playerService.getCurrentUser(request);
        SearchResponse<BallBet> search = betService.search(query,currentUser, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
    @ApiOperation(
            value = "订单中心-撤消",
            notes = "订单中心-撤消" ,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "betId",value = "订单ID",required = true)
    })
    @GetMapping("unbet")
    public Object unbet(Long betId,
                        HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer currentUser = playerService.getCurrentUser(request);
        BaseResponse response = betService.unbet(betId,currentUser);
        return response;
    }
    @ApiOperation(
            value = "订单中心-详情",
            notes = "订单中心-详情" ,
            httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "betId",value = "订单ID",required = true)
    })
    @GetMapping("betInfo")
    public Object betInfo(Long betId,
                        HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer currentUser = playerService.getCurrentUser(request);
        BaseResponse response = betService.betInfo(betId,currentUser);
        return response;
    }

    @ApiOperation(
            value = "充值",
            notes = "充值" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money",value = "充值金额",required = true),
    })
    @PostMapping("recharge")
    public Object recharge(Long money,
                        HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer currentUser = playerService.getCurrentUser(request);
        BaseResponse response = playerService.recharge(currentUser, money);
        return BaseResponse.successWithData(response);
    }
    @ApiOperation(
            value = "提现",
            notes = "提现" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money",value = "提现金额",required = true),
    })
    @PostMapping("withdrawal")
    public Object withdrawal(Long money,
                        HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer currentUser = playerService.getCurrentUser(request);
        BaseResponse response = playerService.withdrawal(currentUser, money);
        return BaseResponse.successWithData(response);
    }

    @ApiOperation(
            value = "提现密码是否设置",
            notes = "提现密码是否设置",
            httpMethod = "GET")
    @GetMapping("/getPwdPay")
    public BaseResponse getPwdPay(HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer player = playerService.getCurrentUser(request);
        Map<String,Object> data = new HashMap<>();
        data.put("paySet",StringUtils.isEmpty(player.getPayPassword())?2:1);
        return BaseResponse.successWithData(data);
    }

    @ApiOperation(
            value = "修改提现密码",
            notes = "修改提现密码",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "origin",value = "原密码",required = true),
            @ApiImplicitParam(name = "newpwd",value = "新密码",required = true),
            @ApiImplicitParam(name = "confirmed",value = "再次密码",required = true),
    })
    @PostMapping("/editPwdPay")
    public BaseResponse editPwdPay(@Validated AuthEditPwdRequest req, HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer player = playerService.getCurrentUser(request);

        if(!player.getPayPassword().equals(PasswordUtil.genPasswordMd5(req.getOrigin()))) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("origin","originError"));
        }

        if(!req.getConfirmed().equals(req.getNewpwd())) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("confirmed","confirmedError"));
        }

        BallPlayer edit = BallPlayer.builder()
                .payPassword(PasswordUtil.genPasswordMd5(req.getConfirmed()))
                .build();
        edit.setId(player.getId());
        if(!basePlayerService.editAndClearCache(edit, player)) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("","updateFailed"));
        }
        return new BaseResponse(StatusCodes.OK, "edit success");
    }
    @ApiOperation(
            value = "设置提现密码",
            notes = "设置提现密码",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payPwd",value = "密码",required = true),
            @ApiImplicitParam(name = "payPwdAgain",value = "再次密码",required = true),
    })
    @PostMapping("/setPwdPay")
    public BaseResponse setPwdPay(@Validated AuthSetPayPwdRequest req, HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer player = playerService.getCurrentUser(request);

        if(!req.getPayPwdAgain().equals(req.getPayPwd())) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("confirmed","confirmedError"));
        }

        BallPlayer edit = BallPlayer.builder()
                .payPassword(PasswordUtil.genPasswordMd5(req.getPayPwd()))
                .build();
        edit.setId(player.getId());
        if(!basePlayerService.editAndClearCache(edit, player)) {
            return BaseResponse.failedWithData(BaseResponse.FAIL_FORM_SUBMIT,
                    ResponseMessageUtil.responseMessage("","updateFailed"));
        }
        return new BaseResponse(StatusCodes.OK, "edit success");
    }

    @ApiOperation(
            value = "数据中心",
            notes = "数据中心,totalBalance:团队余额, playerCount:团队人数, newPlayer:新增注册, totalWithdrawal:团队提现, totalRecharge:团队充值, totalBetPlayer:投注人数, netProfit:净利润, cumulativeActivity:活动奖励, playerOffline:未上线, cumulativeWinning:中奖总额, playerActive:活跃人数, totalBetBalance:投注金额, cumulativeDiscount:优惠金额",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username",value = "用户名"),
            @ApiImplicitParam(name = "time",value = "1.今日,2昨日,3.七日,4.10日,5.30日",required = true),
    })
    @PostMapping("/data_center")
    public BaseResponse dataCenter(@Validated DataCenterRequest dataCenterRequest, HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer player = playerService.getCurrentUser(request);
        Map<String,Object> data = playerService.dataCenter(player,dataCenterRequest);
        return BaseResponse.successWithData(data);
    }
    @ApiOperation(
            value = "数据中心-下级详情",
            notes = "数据中心-下级详情,recharge:充值, levelType:层级, newPlayer:新增, cumulativeReflect:提现",
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "time",value = "1.今日,2昨日,3.七日,4.10日,5.30日",required = true),
    })
    @PostMapping("/data_center/detail")
    public BaseResponse dataCenterDetail(@Validated DataCenterRequest dataCenterRequest, HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer player = playerService.getCurrentUser(request);
        Collection<Map<String, Object>> data = playerService.dataCenterDetail(player, dataCenterRequest);
        return BaseResponse.successWithData(data);
    }
    @ApiOperation(
            value = "返佣中心-统计",
            notes = "返佣中心-统计",
            httpMethod = "GET")
    @GetMapping("/rebate_statis")
    public BaseResponse rebateStatis( HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer player = playerService.getCurrentUser(request);
        BaseResponse data = loggerBackService.statis(player);
        return data;
    }
    @ApiOperation(
            value = "返佣中心-列表",
            notes = "返佣中心-列表,type:1下注返佣 2盈利返佣 3充值返佣,status:1未领取 2已领取" ,
            httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo",value = "页码"),
            @ApiImplicitParam(name = "pageSize",value = "数量")
    })
    @PostMapping("rebate_list")
    public Object rebateList(
                        @RequestParam(defaultValue = "1")Integer pageNo,
                        @RequestParam(defaultValue = "20") Integer pageSize,
                        HttpServletRequest request) throws TokenInvalidedException, PlayerDisabledException {
        BallPlayer currentUser = playerService.getCurrentUser(request);
        SearchResponse<BallLoggerBack> search = loggerBackService.search(currentUser, pageNo, pageSize);
        return BaseResponse.successWithData(search);
    }
//    @ApiOperation(
//            value = "银行卡",
//            notes = "余额变动记录" ,
//            httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "type",value = "1充值 2提现 3投注 4赢 5佣金 6人工"),
//            @ApiImplicitParam(name = "pageNo",value = "页码"),
//            @ApiImplicitParam(name = "pageSize",value = "数量")
//    })
//    @PostMapping("balance_change")
//    public Object index(BalanceChangeRequest balanceChangeRequest,
//                        @RequestParam(defaultValue = "1")Integer pageNo,
//                        @RequestParam(defaultValue = "20") Integer pageSize,
//                        HttpServletRequest request) throws TokenInvalidedException {
//        BallPlayer currentUser = playerService.getCurrentUser(request);
//        SearchResponse<BallBalanceChange> search = ballBalanceChangeService.search(currentUser,balanceChangeRequest, pageNo, pageSize);
//        return BaseResponse.successWithData(search);
//    }
}
