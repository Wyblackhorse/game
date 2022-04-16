package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 账变表
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ball_balance_change")
public class BallBalanceChange implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long createdAt;
    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 变化金额
     */
    private Long changeMoney;

    /**
     * 初始金额
     */
    private Long initMoney;

    /**
     * 变化后的金额
     */
    private Long dnedMoney;

    /**
     * 1充值 2提现 3投注 4赢 5佣金 6人工 
     */
    private Integer balanceChangeType;

    /**
     * 备注
     */
    private String remark;


}
