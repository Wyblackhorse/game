package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.apache.commons.lang3.StringUtils;


/**
 * <p>
 * 玩家账号
 * </p>
 *
 * @author oxo_jy
 * @since 2022-04-13
 */
@Data
@TableName("ball_player")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BallPlayer extends BaseDAO {

    private static final long serialVersionUID = 1L;

    @Version
    private Long version;

    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    /**
     * 邀请码(每个账户生成的时候就会生成唯一性)
     */
    private String invitationCode;

    /**
     * 直属上级(添加索引)
     */
    private Long superiorId;
    /**
     * 直属上级username
     */
    private String superiorName;

    /**
     * 状态1正常 2封禁
     */
    private Integer status;

    /**
     * 账号类型 1测试号 2正常号 3代理账号
     */
    private Integer accountType;

    /**
     * 钱包余额(2位小数/100)
     */
    private Long balance;

    /**
     * token  用户的唯一标识
     */
    private String token;

    /**
     * 最新的一次登录ip
     */
    private String theNewIp;

    /**
     * 上一次登录的ip
     */
    private String theLastIp;

    /**
     * 会员的级别  比如他的上一级是 3  他就是4
     */
    private Integer vipLevel;

    /**
     * 累计中奖
     */
    private Long cumulativeWinning;

    /**
     * 推广收入
     */
    private Long promoteIncome;

    /**
     * 电子邮箱
     */
    private String eMail;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 团队人数
     */
    private Integer groupSize;

    /**
     * 累计提现
     */
    private Long cumulativeReflect;
    /**
     * 提现次数
     */
    private Integer reflectTimes;
    /**
     * 最大提现金额
     */
    private Long maxReflect;
    /**
     * 首次提现金额
     */
    private Long firstReflect;

    /**
     * 累计充值
     */
    private Long cumulativeTopUp;
    /**
     * 充值次数
     */
    private Integer topUpTimes;
    /**
     * 最大的充值金额
     */
    private Long maxTopUp;

    /**
     * 首次充值金额
     */
    private Long firstTopUp;

    /**
     * 首次充值金额时间
     */
    private Long firstTopUpTime;

    /**
     * 线上充值金额
     */
    private Long onLineTopUp;

    /**
     * 线下充值金额
     */
    private Long offlineTopUp;

    /**
     * 人工加款
     */
    private Long artificialAdd;

    /**
     * 人工减款
     */
    private Long artificialSubtract;

    /**
     * 累计打码量
     */
    private Long cumulativeQr;

    /**
     * 离下次提现所需要的打码量
     */
    private Long needQr;

    /**
     * 累计投注金额
     */
    private Long accumulativeBet;

    /**
     * 累计反水
     */
    private Long cumulativeBackWater;

    /**
     * 直属下级个数
     */
    private Integer directlySubordinateNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 层级关系树
     * _id_id_..._id_,
     */
    private String superTree;


    @TableField(exist = false)
    private String editPwd;

    @TableField(exist = false)
    private String ip;


    public Integer getLevelStr(){
        if(StringUtils.isEmpty(superTree)){
            return 0;
        }
        if("_".equals(superTree)){
            return 1;
        }
        return superTree.split("_").length;
    }
}
