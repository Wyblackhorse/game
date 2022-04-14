package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 财务账单
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_record")
public class BallRecord extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 玩家id
     */
    private Long playerId;

    /**
     * 金额
     */
    private Long money;

    /**
     * 账单类型 1线下充值 2线上充值 3提现 4佣金  5奖励的金额 6人工加款 
     */
    private Integer recordType;

    /**
     * 状态 1正常(审核通过) 2待审核 3审核不通过
     */
    private Integer status;

    /**
     * 备注(比如审核不通过的原因)
     */
    private String remark;


}
