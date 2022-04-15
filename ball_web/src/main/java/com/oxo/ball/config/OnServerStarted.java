package com.oxo.ball.config;

import com.oxo.ball.service.admin.IBallSystemConfigService;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("on server start~version:{}",1);
        systemConfigService.init();
    }
}
