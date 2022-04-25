package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 日志表-下注日志
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_logger_bet")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BallLoggerBet implements Serializable {

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
    private String betContent;

    /**
     * 操作的ip
     */
    private String betIp;
    /**
     * 订单号
     */
    private Long betOrderNo;
    /**
     * 登录时间
     */
    private Long createdAt;

}
