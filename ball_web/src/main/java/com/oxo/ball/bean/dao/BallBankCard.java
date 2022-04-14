package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 银行卡
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_bank_card")
public class BallBankCard extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 玩家ID
     */
    private Long playerId;

    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 银行名字
     */
    private String bankName;

    /**
     * 银行编码
     */
    private String backEncoding;

    /**
     * 持卡人姓名
     */
    private String cardName;

    /**
     * 国际
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 支行
     */
    private String subBranch;

    /**
     * 状态 1正常 2禁用
     */
    private Integer status;

    /**
     * usdt 地址
     */
    private Integer backCardType;

    /**
     * 协议
     */
    private String protocol;


}
