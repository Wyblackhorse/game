package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiFixture {
    private Long id;
    private String referee;
    private String timezone;
    private String date;
    private Long timestamp;
    private Status status;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status{
        @JsonProperty("long")
        private String longStatus;
        @JsonProperty("short")
        private String shortStatus;
        /**
         * 用时
         */
        private Integer elapsed;
    }
}
