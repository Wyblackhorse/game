package com.oxo.ball.bean.dao;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.oxo.ball.bean.dao.BaseDAO;
import java.io.Serializable;
import java.sql.Blob;

import com.oxo.ball.bean.valid.Create;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

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
    @NotEmpty
    @Size(min=5,max=16,message = "用户名长度为5-16")
    @Pattern(regexp = "\\w+",message = "用户名必须是字母数字")
    private String username;

    /**
     * 密码
     * create只是1个标识,当Valide里面指定了才会验证
     * groups = {Default,Create}
     */
    @Size(min=6,max=16,message = "密码长度为6-16",groups = {Default.class,Create.class})
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
     * 状态1正常 2封禁
     */
    private Boolean status;

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
     * 累计提现
     */
    private Long cumulativeReflect;

    /**
     * 上一次登录的ip
     */
    private Blob theLastIp;

    /**
     * 会员的级别  比如他的上一级是 3  他就是4
     */
    private Integer vipLevel;

    /**
     * 累计充值
     */
    private Long cumulativeTopUp;

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
     * 最大提现金额
     */
    private Long maxReflect;

    /**
     * 最大的充值金额
     */
    private Long maxTopUp;

    /**
     * 首次提现金额
     */
    private Long firstReflect;

    /**
     * 首次充值金额
     */
    private Long firstTopUp;

    /**
     * 首次充值金额时间
     */
    private Long firstTopUpTime;

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
     * 累计反水
     */
    private Long cumulativeBackWater;

    /**
     * 直属下级个数
     */
    private Integer directlySubordinateNum;

    /**
     * 提现次数
     */
    private Integer reflectTimes;


}
