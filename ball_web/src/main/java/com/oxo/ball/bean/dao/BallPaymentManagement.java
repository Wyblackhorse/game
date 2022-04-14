package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 支付管理
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_payment_management")
public class BallPaymentManagement extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 支付渠道 id  (显示三个参数 在前段)
     */
    private String channelOfDisbursementId;

    /**
     * 商户别名
     */
    private String shopperNickname;

    /**
     * 前台是否显示 1显示 2不显示
     */
    private Integer frontDisplay;

    /**
     * 代收秘钥
     */
    private String collectionKey;

    /**
     * 代付秘钥
     */
    private String payKey;

    /**
     * 停用金额
     */
    private Long stopMoney;

    /**
     * 自动汇率 1开启 2关闭
     */
    private Integer automaticExchangeRate;

    /**
     * 汇率(自动汇率打开 这个不生效)
     */
    private String exchangeRate;

    /**
     * 最高入款金额
     */
    private String maxGetMoney;

    /**
     * 最低入款金额
     */
    private Long minGetMoney;

    /**
     * 过期时间 订单
     */
    private Long expirationTime;

    /**
     * 前台说明
     */
    private String frontExplain;


}
