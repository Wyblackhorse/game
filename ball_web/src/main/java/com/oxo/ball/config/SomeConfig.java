package com.oxo.ball.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "some-comfig")
@Data
@Configuration
public class SomeConfig {
}
