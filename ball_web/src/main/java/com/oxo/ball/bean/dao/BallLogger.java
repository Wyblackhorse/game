package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 日志表
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_logger")
public class BallLogger extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 日志种类 1下注日志(玩家) 2登录日志 3操作日志
     */
    private Integer loggerKinkds;

    /**
     * 管理员id  默认为0
     */
    private Long adminId;

    /**
     * 玩家日志 默认为 0
     */
    private Long playerId;

    /**
     * 日志 内容 
     */
    private String content;

    /**
     * 操作的ip
     */
    private String ip;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 设备型号
     */
    private String unitType;


}
