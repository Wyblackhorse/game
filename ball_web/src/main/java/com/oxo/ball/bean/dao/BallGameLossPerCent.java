package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 游戏赔率
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Getter
@Setter
@TableName("ball_game_loss_per_cent")
public class BallGameLossPerCent extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 游戏id 
     */
    private Long gameId;

    /**
     * 比分
     */
    private String score;

    /**
     * 比赛类型 1全场 2上半场
     */
    private Integer gameType;

    /**
     * 赔率
     */
    private Integer lossPerCent;

    /**
     * 是否保本 1保本 2不保本(默认为2)
     */
    private String even;

    /**
     * 状态 1开启  2关闭
     */
    private Integer status;

    /**
     *  *-4说明，客赢4球(含)以上
     *  4-*     主赢4球(含)以上
     */
    @TableField(exist = false)
    private String help;

    public String getHelp(){
        if(score.startsWith("*")){
            return "客赢"+score.split("-")[1]+"球(含)以上";
        }
        if(score.endsWith("*")){
            return "主赢"+score.split("-")[0]+"球(含)以上";
        }
        return "";
    }
}
