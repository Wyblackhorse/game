package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 日志表-登录日志
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_logger_login")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BallLoggerLogin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 玩家用户名
     */
    private String playerName;
    /**
     * 顶级用户名
     */
    private String superPlayerName;

    /**
     * 登录设备
     */
    private String devices;

    /**
     * 操作的ip
     */
    private String ip;
    /**
     * 登录时间
     */
    private Long createdAt;

}
