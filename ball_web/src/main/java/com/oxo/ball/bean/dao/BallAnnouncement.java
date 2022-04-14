package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 轮播公告
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_announcement")
public class BallAnnouncement extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 提现名称
     */
    private String content;

    /**
     * 语言编码
     */
    private String language;

    /**
     * 1正常 2禁用
     */
    private Integer status;


}
