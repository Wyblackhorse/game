package com.oxo.ball.bean.dto.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author flooming
 */
@Data
public class AuthLoginResponse implements Serializable {
    private static final long serialVersionUID = -13164954870660844L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("user")
    private String userName;

    @JsonProperty("nickname")
    private String nickName;

    @JsonProperty("role")
    private Integer role;

    @JsonProperty("token")
    private String token;
}
