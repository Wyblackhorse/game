package com.oxo.ball.bean.dto.queue;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NegativeOrZero;
import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageQueueDTO implements Serializable {
    public static final int TYPE_LOG_LOGIN = 1;
    public static final int TYPE_LOG_OPER = 2;
    public static final int TYPE_LOG_BET = 3;
    private Integer type;
    private Object data;
}
