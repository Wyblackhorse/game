package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class DataCenterRequest {

    private String username;
    @NotNull
    @Min(value = 1,message = "timeError")
    @Max(value = 5,message = "timeError")
    private Integer time;
}
