package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author ASUS
 * 权限表
 */
@Data
@TableName(value = "ball_menu")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallMenu extends BaseDAO implements Serializable {
    private static final long serialVersionUID = 3197834341055202391L;
    /**
     * 上级菜单，0为父级
     */
    private Long parentId;
    /**
     * 菜单名
     */
    private String menuName;
    /**
     * 请求路径
     */
    private String path;
    /**
     * 是否菜单
     */
    private Integer isMenu;

    /**
     * 1.正常 2禁用
     */
    private Integer status;

    /**
     * 下级菜单
     */
    @TableField(exist = false)
    private List<BallMenu> subMemu;
}
