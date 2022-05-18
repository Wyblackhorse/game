package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiLeague implements Serializable {
    private Long id;
    private String name;
    private String type;
    private String logo;
}
