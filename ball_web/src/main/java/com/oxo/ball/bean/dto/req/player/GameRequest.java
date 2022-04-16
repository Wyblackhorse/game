package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class GameRequest {
    /**
     * 开赛时间
     * 0 全部 1今天 2明天
     */
    @NotNull
    private Integer startTime;
    /**
     * 赛事状态
     * 0 未开始或者未结束
     * 1.已结束
     */
    @NotNull
    private Integer status;
}
