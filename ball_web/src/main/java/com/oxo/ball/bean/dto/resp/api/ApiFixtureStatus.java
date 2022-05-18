package com.oxo.ball.bean.dto.resp.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiFixtureStatus {
    public static final String STATUS_TBD = "TBD"; //待定：时间待定
    public static final String STATUS_NS = "NS";//NS : 未开始
    public static final String STATUS_1H = "1H";//1H：上半场，开球
    public static final String STATUS_HT = "HT";//HT : 中场休息
    public static final String STATUS_2H = "2H";//2H：下半场，下半场开始
    public static final String STATUS_ET = "ET";//ET：加时赛
    public static final String STATUS_P = "P";//P : 处罚进行中
    public static final String STATUS_FT = "FT";//FT : 比赛结束
    public static final String STATUS_AET = "AET";//AET : 加时赛结束
    public static final String STATUS_PEN = "PEN";//PEN : 点球后比赛结束
    public static final String STATUS_BT = "BT";//BT：休息时间（加时赛）
    public static final String STATUS_SUSP = "SUSP";//SUSP : 比赛暂停
    public static final String STATUS_INT = "INT";//INT：匹配中断
    public static final String STATUS_PST = "PST";//PST：比赛推迟
    public static final String STATUS_CANC = "CANC";//CANC : 比赛取消
    public static final String STATUS_ABD = "ABD";//ABD : 比赛被放弃
    public static final String STATUS_AWD = "AWD";//AWD：技术损失
    public static final String STATUS_WO = "WO";//WO : 走过去
    public static final String STATUS_LIVE = "LIVE";//直播：进行中*
}
