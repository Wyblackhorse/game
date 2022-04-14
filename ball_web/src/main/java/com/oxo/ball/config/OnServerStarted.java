package com.oxo.ball.config;

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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("on server start~version:{}",1);
    }
}
