package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 下注
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_bet")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     * 1 未结算 2已结算 3撤单  4回滚
     */
    private Integer status;

    /**
     * 订单号-年-月-日+1000001
     * yyyyMMdd
     *
     */
    private Long orderNo;

    /**
     * 结算时间
     */
    private Long settlementTime;
    /**
     * 下注类型
     * 1正波
     * 2反波
     */
    private Integer betType;

    /**
     * 备注
     */
    private String remark;
}
