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
    @NotNull(message = "startTimeIsNull")
    @Min(value = 0,message = "startTimeError")
    @Max(value = 2,message = "startTimeError")
    private Integer startTime;
    /**
     * 赛事状态
     * 0 未开始
     * 1 已开始
     * 2.已结束
     */
    @NotNull
    @Min(value = 0,message = "statusError")
    @Max(value = 2,message = "statusError")
    private Integer status;
}
