package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GameRequest {
    /**
     * 开赛时间
     * 0 全部 1今天 2明天
     */
    @NotNull
    @Min(value = 0,message = "必须")
    @Max(value = 2,message = "")
    private Integer startTime;
    /**
     * 赛事状态
     * 0 未开始或者未结束
     * 1.已结束
     */
    @NotNull
    @Min(0)
    @Max(1)
    private Integer status;
}
