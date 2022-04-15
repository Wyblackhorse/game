package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author flooming
 * 系统账号表
 */
@Data
@TableName(value = "ball_admin")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallAdmin extends BaseDAO implements Serializable {
    private static final long serialVersionUID = 3197834341055202396L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField("admin_name")
    private String username;
    @JsonIgnore
    @TableField("admin_password")
    private String password;
    @NotNull
    @Size(min=5,max=10,message = "昵称长度在5-10")
    private String nickname;
    private String token;
    private String googleCode;
    /**
     * 1正常 2禁用
     */
    private Integer status;
    @TableField("menu_group_id")
    private Long roleId;

    @TableField(exist = false)
    private BallGroup role;

}
