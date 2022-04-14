package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 下注
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_bet")
public class BallBet extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏id 
     */
    private Long gameId;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 游戏赔率 id(索引)
     */
    private Long gameLossPerCentId;

    /**
     * 下住金额
     */
    private Long betMoney;

    /**
     * 手续费
     */
    private Long handMoney;

    /**
     * 中奖金额
     */
    private Long winningAmount;

    /**
     * 1 未结算 2已结结算 3撤单  4回滚
     */
    private Integer status;


}
