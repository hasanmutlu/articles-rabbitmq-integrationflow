package com.hasanmutlu.articles.integrationflow.integrationflow.services;

import com.hasanmutlu.articles.integrationflow.integrationflow.dto.LogDto;

import java.util.List;

public interface ILogService {

    void sendLogMessages(LogDto log);
    void sendLogMessages(List<LogDto> logs);

}
