package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 存款策略
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_deposit_policy")
public class BallDepositPolicy extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠名称
     */
    private String name;

    /**
     * 优惠类型 1首冲 2每次
     */
    private Integer depositPolicyType;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;

    /**
     * 优惠标准
     */
    private String preferentialStandard;

    /**
     * 优惠百分比
     */
    private String preferentialPer;

    /**
     * 优惠上限
     */
    private String preferentialTop;

    /**
     * 状态 1开启 2关闭
     */
    private Integer status;

    /**
     * 协议
     */
    private String remark;


}
