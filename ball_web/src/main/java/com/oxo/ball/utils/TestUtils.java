package com.oxo.ball.utils;


import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TestUtils {

    public static void main(String[] args) {
        String ptree = "_1_2_";
        String stree = "_1_2_3_4_";
        String substring = stree.substring(ptree.length());
        System.out.println(Arrays.toString(substring.split("_")));
//        System.out.println(BigDecimalUtil.antiPerCent("99.96"));
//        List<String> scores = new ArrayList<>();
//        scores.add("1");
//        scores.add("10");
//        scores.add("12");
//        scores.add("13");
//        scores.add("*");
//        Collections.sort(scores, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                if(o1.equals("*")){
//                    return 1;
//                }
//                return Integer.parseInt(o1)-Integer.parseInt(o2);
//            }
//        });
//        System.out.println(scores);
        // de8776116c28303c8d03be3b4e0cdfc9
//        System.out.println(PasswordUtil.genPasswordMd5("1234567"));
//        System.out.println(PasswordUtil.genPasswordMd5("123456"));
//        System.out.println("_1_".substring(0));
//        Double odds = Double.valueOf("0.5");
//        double div = BigDecimalUtil.div(odds, 100);
//        long bingo = Double.valueOf(BigDecimalUtil.mul(div, 21100)).longValue();
//        Long oddsLong = Double.valueOf(Double.valueOf("5")*100).longValue();
//        Long bingo = 20000+21100/10000*oddsLong;
//        System.out.println(21100+bingo);
//        edit.setWinningAmount(bet.getBetMoney()+bet.getBetMoney()*oddsLong);

//        System.out.println("1".equals(String.valueOf(1)));
//        System.out.println(TimeUtil.TIME_ONE_DAY);
//        long curr = TimeUtil.getNowTimeMill();
//        for(int i=0;i<20;i++){
//            if(i%2==0){
//                System.out.println("INSERT INTO `ball_game` (`game_logo`, `alliance_logo`, `alliance_name`, `main_logo`, `main_name`, `guest_logo`, `guest_name`, `start_time`, `score`, `game_status`, `top`, `hot`, `even`, `status`, `created_at`, `updated_at`) VALUES ('', '', 'Salted fish', '', 'holy shit', '', 'kick ass', '"+(curr-((i%7)*TimeUtil.TIME_ONE_DAY))+"', '1-1/(0-0)', '3', '2', '1', '2', '3', '"+(curr-((i%7)*TimeUtil.TIME_ONE_DAY))+"', '1651133045779');");
//            }else{
//                System.out.println("INSERT INTO `ball_game` (`game_logo`, `alliance_logo`, `alliance_name`, `main_logo`, `main_name`, `guest_logo`, `guest_name`, `start_time`, `score`, `game_status`, `top`, `hot`, `even`, `status`, `created_at`, `updated_at`) VALUES ('', '', 'Salted fish', '', 'holy shit', '', 'kick ass', '"+(curr-((i%7)*TimeUtil.TIME_ONE_DAY))+"', '0-1/(0-0)', '3', '2', '1', '2', '3', '"+(curr-((i%7)*TimeUtil.TIME_ONE_DAY))+"', '1651133045779');");
//            }
//        }
    }
}
