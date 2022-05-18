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
 * 日志表-返佣日志
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_logger_back")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BallLoggerBack implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户ID
     */
    private Long playerId;
    /**
     * 1 下注返佣 2盈利返佣 3充值返佣
     * 个人中心统计
     * 今日 昨日 上周
     *  - 列表 日期 返佣类型 返佣金额 操作
     */
    private Integer type;

    /**
     * 金额
     */
    private Long money;

    /**
     * 状态 1未领取 2已领取
     */
    private Integer status;
    /**
     * 操作时间
     */
    private Long createdAt;

}
