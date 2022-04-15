package com.oxo.ball.bean.dto.req.player;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PlayerRegistRequest {
    @NotEmpty
    @Size(min = 5, max = 16, message = "用户名长度为5-16")
    @Pattern(regexp = "\\w+", message = "用户名必须是字母数字")
    private String username;
    /**
     * create只是1个标识,当Valide里面指定了才会验证
     * groups = {Default,Create}
     */
    @NotEmpty(message = "密码不能为空")
    @Size(min = 6, max = 16, message = "密码长度为6-16")
    private String password;
    @NotEmpty(message = "确定密码不能为空")
    private String twoPassword;
    @NotEmpty(message = "验证码不能为空")
    private String code;
    @NotEmpty
    private String verifyKey;
    private String invitationCode;
}
