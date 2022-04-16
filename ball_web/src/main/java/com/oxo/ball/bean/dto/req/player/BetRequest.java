package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class BetRequest {
    @NotNull
    private Long gameId;
    @NotNull
    private Long oddsId;
    @NotNull
    @Min(1)
    private Long money;

}
