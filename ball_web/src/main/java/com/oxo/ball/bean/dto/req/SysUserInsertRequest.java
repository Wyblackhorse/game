package com.oxo.ball.bean.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author none
 */
@Data
public class SysUserInsertRequest implements Serializable {
    private static final long serialVersionUID = -5546332483963585710L;

    private Long puserId;
    private String username;
    private String password;
    private String nickname;
    private Long roleId;
    private String remark;

}
