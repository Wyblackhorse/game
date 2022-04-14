package com.oxo.ball.bean.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author flooming
 */
@Data
public class AuthLoginRequest implements Serializable {
    private static final long serialVersionUID = -4842392920233760571L;

    @JsonProperty("user")
    private String username;

    @JsonProperty("pwd")
    private String password;

    private Integer isAdmin;
}
