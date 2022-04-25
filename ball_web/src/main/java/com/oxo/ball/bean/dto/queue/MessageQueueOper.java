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
public class MessageQueueOper implements Serializable {
    private String mainOper;
    private String subOper;
    private String remark;
    private String username;
    private String ip;
}
