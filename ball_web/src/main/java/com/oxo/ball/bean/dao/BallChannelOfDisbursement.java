package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 支付/提现渠道
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_channel_of_disbursement")
public class BallChannelOfDisbursement extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 支付渠道名字
     */
    private String name;

    /**
     * 支付类型  自行填写
     */
    private Integer disbursementzhType;

    /**
     * 通道类型 1 线上支付 2线下支付 
     */
    private Integer channelType;

    /**
     * 1 支付渠道 2提现渠道
     */
    private Integer kinds;

    /**
     * 状态 1开启 2关闭
     */
    private Integer status;


}
