package com.oxo.ball.bean.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author flooming
 */
@Data
public class SearchRequest implements Serializable {
    private static final long serialVersionUID = -4811938895508061774L;

    @JsonProperty("keyword")
    private String keyword;

    @JsonProperty("pageNo")
    private Integer pageNo=1;

    @JsonProperty("pageSize")
    private Integer pageSize=20;

    @JsonProperty("options")
    private Object options;

}
