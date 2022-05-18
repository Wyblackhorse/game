package com.oxo.ball.bean.dto.req.player;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author flooming
 */
@Data
public class PlayerAuthLoginRequest implements Serializable {
    private static final long serialVersionUID = -4842392920233760571L;

    @NotEmpty(message = "usernameIsEmpty")
    private String username;
    @NotEmpty(message = "passwordIsEmpty")
    private String password;
    @NotEmpty(message = "codeIsEmpty")
    private String code;
    @NotEmpty(message = "verifyKeyIsEmpty")
    private String verifyKey;
}
