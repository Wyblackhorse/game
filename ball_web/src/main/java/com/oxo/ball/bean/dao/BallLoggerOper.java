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
 * 日志表-操作日志
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_logger_oper")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BallLoggerOper implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 管理模块
     */
    private String mainFunc;
    /**
     * 操作方法
     */
    private String subFunc;

    /**
     * 备注
     */
    private String remark;

    /**
     * 操作的ip
     */
    private String ip;
    /**
     * 操作人
     */
    private String username;
    /**
     * 操作时间
     */
    private Long createdAt;

}
