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
     * 1线上充值 2提现 3投注 4投注盈利 5促销反利
     * 6人工加款 7投注撤消
     * 8人工减款 9投注返奖扣除 10 充值撤消 11线下充值
     * 12提现退回 13投注退回 14投注回滚 15注册赠送
     * 16分享赠送 17 VIP礼金 18 活动礼金 19充值赠送
     *
     */
    private Integer balanceChangeType;

    /**
     * 备注
     */
    private String remark;


}
