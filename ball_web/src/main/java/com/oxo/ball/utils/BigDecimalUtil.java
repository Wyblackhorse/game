package com.oxo.ball.utils;

import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 计算工具类
 */
public class BigDecimalUtil {

    /**
     * 玩家实际存储金额值为*本值
     */
    public static Long PLAYER_MONEY_UNIT = 100L;

    /**
     * 乘法
     *
     * @param num
     * @param addNum
     * @return
     */
    public static long multiply(String num, String addNum) {
        return Long.parseLong(num) * Long.parseLong(addNum);
    }

    /**
     * 乘法
     *
     * @param num
     * @param addNum
     * @return
     */
    public static long multiply(long num, long addNum) {
        return num * addNum;
    }


    /**
     * 求和 取整 后三位
     *
     * @param num
     * @param addNum
     * @return
     */
    public static long add(String num, String addNum) {
        return (Long.parseLong(num) + Long.parseLong(addNum));
    }

    public static long add(long num, long addNum) {
        return (num + addNum);
    }

    /**
     * 求差  取整 后三位
     *
     * @param num
     * @param subNum
     * @return
     */
    public static long sub(String num, String subNum) {
        return (Long.parseLong(num) - Long.parseLong(subNum));
    }

    public static long sub(long num, long subNum) {
        return num - subNum;
    }

    /**
     * 除
     */
    public static long divide(long num, long subNum) {
        return num / subNum;
    }

    public static long objToLongMu100(Object num) {
        return Long.parseLong(num.toString()) * 1000;
    }

    public static long objToLong(Object num) {
        return Long.parseLong(num.toString());
    }


    private static final int DEF_DIV_SCALE = 10;

    //相加
    public static double add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();

    }

    //相减
    public static double sub(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();

    }

    //相乘
    public static double mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();

    }

    //相除
    public static double div(double d1, double d2) {

        return div(d1, d2, DEF_DIV_SCALE);

    }

    public static double div(double d1, double d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 保留3位小数.系统默认
     * @param l
     * @param i
     * @return
     */
    public static String divStr(long l, int i) {
        if (i < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Long.toString(l));
        BigDecimal b2 = new BigDecimal(Long.toString(i));
        return b1.divide(b2, 3, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 保留指定精度位小数
     * @param l
     * @param i
     * @param scale
     * @return
     */
    public static String divStr(long l, int i,int scale) {
        if (i < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Long.toString(l));
        BigDecimal b2 = new BigDecimal(Long.toString(i));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 格式化数字
     */
    public static DecimalFormat decimalFormat = new DecimalFormat("#.###");
    public static String formatNumber(double num){
        return decimalFormat.format(num);
    }

    public static String antiPerCent(String odd) {
        //反波 = 1/正波*40
        Double aDouble = Double.valueOf(odd);
        double div = mul(div(1, aDouble,2),40);
        return String.valueOf(div);
    }
}
