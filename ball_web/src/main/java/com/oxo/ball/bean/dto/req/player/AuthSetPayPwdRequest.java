package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author none
 */
@Data
public class AuthSetPayPwdRequest implements Serializable {
    private static final long serialVersionUID = -4189898441509940210L;

    @NotEmpty(message = "pwdIsEmpty")
    @Size(min = 6, max = 16, message = "passwordSizeError")
    private String payPwd;

    @NotEmpty(message = "confirmedIsEmpty")
    @Size(min = 6, max = 16, message = "passwordSizeError")
    private String payPwdAgain;

}
