package com.oxo.ball.bean.dto.req.player;

import io.swagger.models.auth.In;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BetRequest {
    @NotNull(message = "gameIdIsNull")
    private Long gameId;
    @NotNull(message = "oddsIdIsNull")
    private Long oddsId;
    @NotNull(message = "moneyIsNull")
    @Min(value = 1,message = "moneyError")
    private Long money;
    /**
     * 1正波2反波
     */
    @NotNull(message = "typeIsNull")
    @Range(min = 1,max = 2,message = "typeError")
    private Integer type;

}
