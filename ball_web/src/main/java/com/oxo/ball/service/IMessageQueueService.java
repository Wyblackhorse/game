package com.oxo.ball.service;

import com.oxo.ball.bean.dto.queue.MessageQueueDTO;

public interface IMessageQueueService {
    void putMessage(MessageQueueDTO message);
    void startQueue();
}
