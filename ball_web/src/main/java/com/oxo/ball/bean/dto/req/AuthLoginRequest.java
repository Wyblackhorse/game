package com.oxo.ball.bean.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author flooming
 */
@Data
public class AuthLoginRequest implements Serializable {
    private static final long serialVersionUID = -4842392920233760571L;

    @NotEmpty
    @JsonProperty("user")
    private String username;
    @NotEmpty
    @JsonProperty("pwd")
    private String password;
    @NotEmpty
    private String googleCode;
    @NotEmpty
    private String googleKey;

}
