package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 提现方式
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_withdraw_management")
public class BallWithdrawManagement extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 提现渠道
     */
    private Long channelOfDisbursementId;

    /**
     * 提现名称
     */
    private String name;

    /**
     * 图片地址
     */
    private String iamgeUrl;

    /**
     * 语言编码
     */
    private String language;

    /**
     * 1启用 2关闭
     */
    private Integer status;


}
