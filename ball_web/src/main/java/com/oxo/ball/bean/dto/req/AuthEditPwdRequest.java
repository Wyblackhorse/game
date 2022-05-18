package com.oxo.ball.bean.dto.req;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author none
 */
@Data
public class AuthEditPwdRequest implements Serializable {
    private static final long serialVersionUID = -4189898441509940210L;

    @NotEmpty(message = "originIsEmpty")
    private String origin;
    @NotEmpty(message = "newpwdIsEmpty")
    @Size(min = 6, max = 16, message = "passwordSizeError")
    private String newpwd;
    @Size(min = 6, max = 16, message = "passwordSizeError")
    @NotEmpty(message = "confirmedIsEmpty")
    private String confirmed;
}
