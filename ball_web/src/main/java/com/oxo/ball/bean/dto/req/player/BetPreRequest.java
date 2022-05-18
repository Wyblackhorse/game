package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BetPreRequest {
    @NotNull(message = "gameIdIsNull")
    private Long gameId;
    @NotNull(message = "oddsIdIsNull")
    private Long oddsId;
}
