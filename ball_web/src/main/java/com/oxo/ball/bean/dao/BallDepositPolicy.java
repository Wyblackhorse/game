package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import java.util.Date;

import com.oxo.ball.utils.TimeUtil;
import lombok.*;

/**
 * <p>
 * 存款策略
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_deposit_policy")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BallDepositPolicy extends BaseDAO {

    private static final long serialVersionUID = 1L;

    public static final int DEPOSITPOLICY_TYPE_FIRST = 1;
    public static final int DEPOSITPOLICY_TYPE_EVERY = 2;

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
    private Integer preferentialPer;

    /**
     * 优惠上限
     */
    private Integer preferentialTop;

    /**
     * 状态 1开启 2关闭
     */
    private Integer status;

    /**
     * 协议
     */
    private String remark;


    public String getStartStr() {
        return TimeUtil.dateFormat(new Date(getStartTime()),TimeUtil.TIME_YYYY_MM_DD_HH_MM_SS);
    }

    public String getEndStr() {
        return TimeUtil.dateFormat(new Date(getEndTime()),TimeUtil.TIME_YYYY_MM_DD_HH_MM_SS);
    }

    @TableField(exist = false)
    private String start;
    @TableField(exist = false)
    private String end;


}
