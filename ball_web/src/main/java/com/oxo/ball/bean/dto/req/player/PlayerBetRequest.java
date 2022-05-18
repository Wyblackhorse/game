package com.oxo.ball.bean.dto.req.player;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Data
public class PlayerBetRequest {
    //今天，昨天，近7日，近10日，近30日，全部/反波胆
    @Min(value = 1,message = "timeError")
    @Max(value = 5,message = "timeError")
    private Integer time;
    // 1查全部 2查反波
    @Range(min = 1,max = 2,message = "typeError")
    private Integer type;
}
