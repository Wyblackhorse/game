package com.oxo.ball.controller.admin;

import com.oxo.ball.bean.dao.BallSystemConfig;
import com.oxo.ball.bean.dto.resp.BaseResponse;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.oxo.ball.service.admin.IBallSystemNoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 系统配置 前端控制器
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@RestController
@RequestMapping("/ball/merchant/param")
public class BallSystemConfigController {

    @Resource
    IBallSystemConfigService systemConfigService;

    @GetMapping()
    public Object getConfig(){
        return BaseResponse.successWithData(systemConfigService.getSystemConfig());
    }

    @PostMapping("loreg")
    public Object loreg(@RequestBody BallSystemConfig systemConfig){
        BallSystemConfig edit = BallSystemConfig.builder()
                .id(systemConfig.getId())
                //是否需要邀请码
                .registerIfNeedVerificationCode(systemConfig.getRegisterIfNeedVerificationCode())
                //密码连续错误次数
                .passwordMaxErrorTimes(systemConfig.getPasswordMaxErrorTimes())
                //锁定时间
                .passwordErrorLockTime(systemConfig.getPasswordErrorLockTime())
                .build();
        Boolean aBoolean = systemConfigService.edit(edit);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @PostMapping("servicer")
    public Object servicer(@RequestBody BallSystemConfig systemConfig){
        BallSystemConfig edit = BallSystemConfig.builder()
                .id(systemConfig.getId())
                .serverUrl(systemConfig.getServerUrl())
                .build();
        Boolean aBoolean = systemConfigService.edit(edit);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @PostMapping("finance")
    public Object finance(@RequestBody BallSystemConfig systemConfig){
        BallSystemConfig edit = BallSystemConfig.builder()
                .id(systemConfig.getId())
                .cardCanNeedNums(systemConfig.getCardCanNeedNums())
                .rechargeCodeConversionRate(systemConfig.getRechargeCodeConversionRate())
                .captchaThreshold(systemConfig.getCaptchaThreshold())
                .betHandMoneyRate(systemConfig.getBetHandMoneyRate())
                .fastMoney(systemConfig.getFastMoney())
                .usdtWithdrawPer(systemConfig.getUsdtWithdrawPer())
                .withdrawUsdtAutomaticPer(systemConfig.getWithdrawUsdtAutomaticPer())
                .evenNeedHandMoney(systemConfig.getEvenNeedHandMoney())
                .maxUsdtAccountNums(systemConfig.getMaxUsdtAccountNums())
                .maxPixAccountNums(systemConfig.getMaxPixAccountNums())
                .withdrawPasswordCanUpdate(systemConfig.getWithdrawPasswordCanUpdate())
                .canWithdrawContinuity(systemConfig.getCanWithdrawContinuity())
                .withdrawPasswordShowNeed(systemConfig.getWithdrawPasswordShowNeed())
                .build();
        Boolean aBoolean = systemConfigService.edit(edit);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @PostMapping("risk")
    public Object risk(@RequestBody BallSystemConfig systemConfig){
        BallSystemConfig edit = BallSystemConfig.builder()
                .id(systemConfig.getId())
                .everydayWithdrawTimes(systemConfig.getEverydayWithdrawTimes())

                .build();
        Boolean aBoolean = systemConfigService.edit(edit);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @PostMapping("operate")
    public Object operate(@RequestBody BallSystemConfig systemConfig){
        BallSystemConfig edit = BallSystemConfig.builder()
                .id(systemConfig.getId())
                .serverUrl(systemConfig.getServerUrl())
                .build();
        Boolean aBoolean = systemConfigService.edit(edit);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }
    @PostMapping("share")
    public Object share(@RequestBody BallSystemConfig systemConfig){
        BallSystemConfig edit = BallSystemConfig.builder()
                .id(systemConfig.getId())
                .serverUrl(systemConfig.getServerUrl())
                .build();
        Boolean aBoolean = systemConfigService.edit(edit);
        return  aBoolean?BaseResponse.SUCCESS:BaseResponse.failedWithMsg("修改失败~");
    }

}
