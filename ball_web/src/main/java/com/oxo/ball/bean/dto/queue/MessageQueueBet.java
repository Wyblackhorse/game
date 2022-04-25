package com.oxo.ball.bean.dto.queue;

import com.oxo.ball.bean.dao.BallPlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageQueueBet implements Serializable {
    private BallPlayer ballPlayer;
    private String ip;
    private String betContent;
    private Long orderId;

}
