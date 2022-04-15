package com.oxo.ball.config;

import com.oxo.ball.auth.AdminAuthInterceptor;
import com.oxo.ball.auth.PlayerAuthInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author flooming
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    private Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Value("${static.file}")
    private String staticFile;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*");
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/ball/**").excludePathPatterns("/static/**"
                ,"/auth/login"
                ,"/auth/logout"
                ,"/auth/userinfo"
                ,"/test/**"
                );
        registry.addInterceptor(authenticationPlayInterceptor())
                .addPathPatterns("/player/**")
                .excludePathPatterns("/player/auth/login",
                        "/player/auth/verify_code",
                        "/player/auth/verify_code_check",
                        "/player/auth/regist");
    }


    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("静态资源目录配置为:"+staticFile);
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/",staticFile);
        super.addResourceHandlers(registry);
    }

    @Bean
    public AdminAuthInterceptor authenticationInterceptor() {
        return new AdminAuthInterceptor();
    }
    @Bean
    public PlayerAuthInterceptor authenticationPlayInterceptor() {
        return new PlayerAuthInterceptor();
    }
}
