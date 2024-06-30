package com.hasanmutlu.articles.integrationflow.integrationflow.services;

import com.hasanmutlu.articles.integrationflow.integrationflow.dto.LogDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogService implements ILogService {

    private final AmqpTemplate logWriterQueueTemplate;

    @Override
    public void sendLogMessages(LogDto log) {
        sendMessageToQueue(log);
    }

    @Override
    public void sendLogMessages(List<LogDto> logs) {
        logs.forEach(this::sendLogMessages);
    }

    private void sendMessageToQueue(LogDto log) {
        logWriterQueueTemplate.convertAndSend(log);
    }

}
