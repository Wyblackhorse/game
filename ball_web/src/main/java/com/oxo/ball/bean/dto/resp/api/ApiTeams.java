package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTeams {
    private Team home;
    private Team away;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team{
        private Long id;
        private String name;
        private String logo;
        private Boolean winner;
    }
}
