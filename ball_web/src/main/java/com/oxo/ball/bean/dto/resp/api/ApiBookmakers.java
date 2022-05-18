package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiBookmakers {
    private Long id;
    private String name;
    private List<ApiBets> bets;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiBets{
        private Long id;
        private String name;
        private List<ApiBetItem> values;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiBetItem{
        private String value;
        private String odd;
    }
}
