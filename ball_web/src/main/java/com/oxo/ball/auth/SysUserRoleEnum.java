package com.oxo.ball.auth;

/**
 * @author flooming
 */
public enum SysUserRoleEnum {
    //未定义
    ROLE_NONE(0),
    //无权限
    ROLE_PASS(-1),
    //登录
    ROLE_LOGIN(1),
    //普通用户
    ROLE_USER(100),
    //管理员
    ROLE_ADMIN(200)
    ;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    SysUserRoleEnum(int id) {
        this.id = id;
    }
}
