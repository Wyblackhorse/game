package com.oxo.ball.scheduled;

import com.oxo.ball.bean.dto.req.player.PlayerBetRequest;
import com.oxo.ball.contant.LogsContant;
import com.oxo.ball.service.admin.IBallGameService;
import com.oxo.ball.service.player.IPlayerBetService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jy
 */
@Component
@EnableScheduling
@Slf4j
public class JobScheduled {

    private Logger apiLog = LoggerFactory.getLogger(LogsContant.API_LOG);


    @Resource
    IPlayerBetService playerBetService;
    @Autowired
    IBallGameService gameService;
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        return taskScheduler;
    }

    /**
     * 检测是否有消息可以发送
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void newDayStart() {
        //清楚今日订单号
        playerBetService.clearDayOrderNo();
    }
    /**
     * 每分钟修改开赛的比赛
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    private void wsSenddingPing() {
        gameService.whenGameStart();
    }



}
