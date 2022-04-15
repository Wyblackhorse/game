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

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String code;
    @NotEmpty
    private String verifyKey;
}
