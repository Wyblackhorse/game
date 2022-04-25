package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 轮播公告
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_announcement")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallAnnouncement extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
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
