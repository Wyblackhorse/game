package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 反佣策略
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_commission_strategy")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BallCommissionStrategy extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠名称
     */
    private String name;

    /**
     * 1 下注返佣 2盈利返佣 3充值返佣
     */
    private Integer commissionStrategyType;

    /**
     * 返佣层级
     */
    private Integer commissionLevel;

    /**
     * 自动发放 1 自动 2不自动
     */
    private Integer automaticDistribution;

    /**
     * 状态 1开启 2关闭
     */
    private Integer status;


}
