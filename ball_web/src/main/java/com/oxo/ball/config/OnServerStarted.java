package com.oxo.ball.config;

import com.oxo.ball.service.IMessageQueueService;
import com.oxo.ball.service.admin.IApiService;
import com.oxo.ball.service.admin.IBallSystemConfigService;
import com.oxo.ball.utils.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jy
 */
@Component
@Slf4j
public class OnServerStarted  implements ApplicationListener<ContextRefreshedEvent>  {

    @Resource
    SomeConfig someConfig;

    @Resource
    IBallSystemConfigService systemConfigService;
    @Resource
    IMessageQueueService messageQueueService;
    @Autowired
    IApiService apiService;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("on server start~version:{}",1);
        systemConfigService.init();
        ThreadPoolUtil.exec(() -> {
            messageQueueService.startQueue();
        });
        if(someConfig.getApiSwitch()==null){
            ThreadPoolUtil.exec(() -> {
                apiService.refreshLeagues();
            });
        }
    }
}
