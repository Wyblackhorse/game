package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiOdds {
    private ApiPaging paging;
    private List<ApiOddsResponse> response;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiOddsResponse{
        private List<ApiBookmakers> bookmakers;
    }
}
