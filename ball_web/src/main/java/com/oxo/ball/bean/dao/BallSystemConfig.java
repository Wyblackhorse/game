package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 系统配置
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_system_config")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallSystemConfig implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 注册是否需要邀请码 1需要 0不需要
     */
    private Integer registerIfNeedVerificationCode;

    /**
     * 验证码 格式 1 纯数字 2串字母 3字母数字
     */
    private Integer verificationCodeLayout;

    /**
     * 密码连续错误的次数
     */
    private Integer passwordMaxErrorTimes;

    /**
     * 密码连续错误锁屏时间(秒)
     */
    private Integer passwordErrorLockTime;

    /**
     * 客服连接
     */
    private String serverUrl;

    /**
     * 会员最多绑卡数量
     */
    private Integer cardCanNeedNums;

    /**
     * 充值打码量转换比例
     */
    private Long rechargeCodeConversionRate;

    /**
     * 用户打码设量设置阀值
     */
    private Long captchaThreshold;

    /**
     * 投注手续费率
     */
    private Long betHandMoneyRate;

    /**
     * 快捷金额
     */
    private Long fastMoney;

    /**
     * usdt 提现汇率
     */
    private Long usdtWithdrawPer;

    /**
     * 提现usdt自动汇率
     */
    private Long withdrawUsdtAutomaticPer;

    /**
     * 保本扣除手续费
     */
    private Long evenNeedHandMoney;

    /**
     * 最多可绑定usdt账号数量
     */
    private Integer maxUsdtAccountNums;

    /**
     * 最多可绑定pix账号数量
     */
    private Integer maxPixAccountNums;

    /**
     * 提现密码是否可以修改 1 可以 2不可以
     */
    private Integer withdrawPasswordCanUpdate;
    /**
     * 是否可以连续发起提现
     */
    private Integer canWithdrawContinuity;

    /**
     * 控制首页提现密码是否可以关闭
     */
    private Integer withdrawPasswordShowNeed;


    /**
     * 每日的提现上线次数
     */
    private Integer everydayWithdrawTimes;


}
