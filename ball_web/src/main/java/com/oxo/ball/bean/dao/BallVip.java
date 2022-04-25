package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ball_vip")
public class BallVip extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 会员等级
     */
    private Integer level;

    /**
     * 名称
     */
    private String levelName;

    /**
     * 最小提现
     */
    private Long minPull;

    /**
     * 最大提现
     */
    private Long maxPull;

    /**
     * 返水（%）
     */
    private Integer backWater;

    /**
     * 礼金
     */
    private Integer cashGift;

    /**
     * 发放周期
     */
    private Integer cashGiftInterval;

    /**
     * 状态0禁1启
     */
    private Integer status;


}
