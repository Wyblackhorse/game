package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiLeagues implements Serializable {
    private ApiPaging paging;
    private List<LeaguesResponse> response;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LeaguesResponse{
        private ApiLeague league;
    }
}
