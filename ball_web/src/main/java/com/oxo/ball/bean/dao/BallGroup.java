package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ASUS
 * 角色表
 */
@Data
@TableName(value = "ball_group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallGroup extends BaseDAO implements Serializable {
    private static final long serialVersionUID = 3197834341055202391L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 角色名
     */
    private String name;

    private Integer status;

    @TableField(exist = false)
    private Long[] authsId;
}
