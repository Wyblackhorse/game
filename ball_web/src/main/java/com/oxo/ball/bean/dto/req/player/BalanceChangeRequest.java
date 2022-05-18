package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class BalanceChangeRequest {
    /**
     * 1充值 2提现 3投注 4赢 5佣金 6人工
     */
    @Min(value = 1,message = "typeError")
    @Max(value = 6,message = "typeError")
    private Integer type;

    /**
     * 1收入2支出
     */
    @Min(value = 1,message = "typebError")
    @Max(value = 2,message = "typebError")
    private Integer typeb;

    /**
     * 1今日2昨日3近7日
     */
    @Min(value = 1,message = "timeError")
    @Max(value = 3,message = "timeError")
    private Integer time;
}
