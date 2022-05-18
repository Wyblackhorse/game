package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class BallGame  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    private Long createdAt;
    private Long updatedAt;
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

//    /**
//     * 比分 0:0/(0-0)  括号库里面是上半场
//     */
//    private String score;
    /**
     *  主半场
     */
    private Integer homeHalf;
    /**
     * 客半场
     */
    private Integer guestHalf;
    /**
     *  主全场
     */
    private Integer homeFull;
    /**
     * 客全场
     */
    private Integer guestFull;
    /**
     *  主加时
     */
    private Integer homeOvertime;
    /**
     * 客加时
     */
    private Integer guestOvertime;
    /**
     *  主点球
     */
    private Integer homePenalty;
    /**
     * 客点球
     */
    private Integer guestPenalty;

    /**
     * 比赛状态 1 没有开始 2正常进行  3结束
     */
    private Integer gameStatus;
    /**
     * 结算类型
     * 0 自动结算
     * 1 手动结算
     * 2 回滚
     * 3 重算
     */
    private Integer settlementType;
    private String settlementRemark;

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

    public String getScore(){
        // 0:0/(0-0)  全场/(上半场)/(加时)/(点球)
        if(getHomeFull()==null){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(homeFull);
        sb.append(":");
        sb.append(guestFull);
        sb.append("/(");
        sb.append(homeHalf);
        sb.append(":");
        sb.append(guestHalf);
        sb.append(")");
        //如果有加时
        if(homeOvertime==null){
            return sb.toString();
        }
        sb.append("/(");
        sb.append(homeOvertime);
        sb.append(":");
        sb.append(guestOvertime);
        sb.append(")");
        //如果有点球
        if(homePenalty==null){
            return sb.toString();
        }
        sb.append("/(");
        sb.append(homePenalty);
        sb.append(":");
        sb.append(guestPenalty);
        sb.append(")");
        return sb.toString();
    }
}
