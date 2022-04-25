package com.oxo.ball.bean.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author none
 */
@Data
public class SysUserEditRequest implements Serializable {
    private static final long serialVersionUID = -7651536801083374366L;

    private Long id;
    private Long roleId;
    private String password;
    private String nickname;
    private String googleCode;
}
