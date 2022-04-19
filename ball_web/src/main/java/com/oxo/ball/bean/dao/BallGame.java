package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;

import lombok.*;

/**
 * <p>
 * 游戏赛事
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("ball_game")
public class BallGame extends BaseDAO {

    private static final long serialVersionUID = 1L;

    /**
     * 联盟logo
     */
    private String allianceLogo;

    /**
     * 联盟名字
     */
    private String allianceName;

    /**
     * 主队logo
     */
    private String mainLogo;

    /**
     * 主队名字
     */
    private String mainName;

    /**
     * 客队logo
     */
    private String guestLogo;

    /**
     * 客队名字
     */
    private String guestName;

    /**
     * 开赛时间
     */
    private Long startTime;

    /**
     * 比分 0:0/(0-0)  括号库里面是上半场
     */
    private String score;

    /**
     * 比赛状态 1 没有开始 2正常进行  3结束
     */
    private Integer gameStatus;

    /**
     * 置顶  1置顶 2 不指定
     */
    private Integer top;

    /**
     * 是否热门 1 热门 2非热门
     */
    private Integer hot;

    /**
     * 是否保本 1保本 2不保本(默认为2)
     */
    private Integer even;

    /**
     * 状态 1开启  2关闭
     */
    private Integer status;


    public Long getRemainingTime(){
        return startTime - System.currentTimeMillis();
    }
}
