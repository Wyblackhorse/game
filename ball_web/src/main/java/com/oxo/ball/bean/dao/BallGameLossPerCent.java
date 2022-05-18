package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 游戏赔率
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_game_loss_per_cent")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BallGameLossPerCent extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏id 
     */
    private Long gameId;

    /**
     * 比分-主场
     */
    private String scoreHome;
    /**
     * 比分-客场
     */
    private String scoreAway;

    /**
     * 比赛类型 1上半场 2全场
     */
    private Integer gameType;

    /**
     * 赔率-正波
     */
    private String lossPerCent;
    /**
     * 赔率-反波
     */
    private String antiPerCent;

    /**
     * 是否保本 1保本 2不保本(默认为2)
     */
    private Integer even;

    /**
     * 状态 1开启  2关闭
     */
    private Integer status;

//    /**
//     *  *-4说明，客赢4球(含)以上
//     *  4-*     主赢4球(含)以上
//     */
//    @TableField(exist = false)
//    private String help;

}
