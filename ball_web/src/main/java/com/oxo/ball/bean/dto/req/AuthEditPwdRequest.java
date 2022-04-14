package com.oxo.ball.bean.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author none
 */
@Data
public class AuthEditPwdRequest implements Serializable {
    private static final long serialVersionUID = -4189898441509940210L;

    private String origin;
    private String newpwd;
    private String confirmed;
}
