package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 轮播图
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_slideshow")
public class BallSlideshow extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String name;

    /**
     * 策略类型 1存款策略 2返佣策略
     */
    private Integer policyType;

    /**
     * 存款策略id
     */
    private Long depositPolicyId;

    /**
     * 返佣策略id
     */
    private Long commissionStrategyId;

    /**
     * 语言编码
     */
    private String language;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 1显示 2不显示(前提是 返佣和 存款策略id 为0)
     */
    private Integer status;


}
