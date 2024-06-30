package com.hasanmutlu.articles.integrationflow.integrationflow.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogDto {
    private String message;
    private String userId;
    private LogType type;
}
