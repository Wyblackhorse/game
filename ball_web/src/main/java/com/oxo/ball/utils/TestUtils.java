package com.oxo.ball.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class TestUtils {

    public static void main(String[] args) {
        long curr = TimeUtil.getNowTimeMill();
        for(int i=0;i<20;i++){
            if(i%2==0){
                System.out.println("INSERT INTO `ball_game` (`game_logo`, `alliance_logo`, `alliance_name`, `main_logo`, `main_name`, `guest_logo`, `guest_name`, `start_time`, `score`, `game_status`, `top`, `hot`, `even`, `status`, `created_at`, `updated_at`) VALUES ('', '', 'Salted fish', '', 'holy shit', '', 'kick ass', '"+(curr-((i%7)*TimeUtil.TIME_ONE_DAY))+"', '1-1/(0-0)', '3', '2', '1', '2', '3', '"+(curr-((i%7)*TimeUtil.TIME_ONE_DAY))+"', '1651133045779');");
            }else{
                System.out.println("INSERT INTO `ball_game` (`game_logo`, `alliance_logo`, `alliance_name`, `main_logo`, `main_name`, `guest_logo`, `guest_name`, `start_time`, `score`, `game_status`, `top`, `hot`, `even`, `status`, `created_at`, `updated_at`) VALUES ('', '', 'Salted fish', '', 'holy shit', '', 'kick ass', '"+(curr-((i%7)*TimeUtil.TIME_ONE_DAY))+"', '0-1/(0-0)', '3', '2', '1', '2', '3', '"+(curr-((i%7)*TimeUtil.TIME_ONE_DAY))+"', '1651133045779');");
            }
        }
    }
}
