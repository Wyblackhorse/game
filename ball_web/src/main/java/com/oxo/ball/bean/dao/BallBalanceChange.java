package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 账变表
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_balance_change")
public class BallBalanceChange extends BaseDAO {

    private static final long serialVersionUID = 1L;

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
