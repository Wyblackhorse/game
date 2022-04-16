package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

@Data
public class BalanceChangeRequest {
    /**
     * 1充值 2提现 3投注 4赢 5佣金 6人工
     */
    private Integer type;
}
