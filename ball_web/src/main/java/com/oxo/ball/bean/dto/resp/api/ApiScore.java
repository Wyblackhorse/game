package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiScore {
    private ApiGoals halftime;
    private ApiGoals fulltime;
    private ApiGoals extratime;
    private ApiGoals penalty;
}

