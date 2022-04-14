package com.oxo.ball;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author jy
 */
@SpringBootApplication
@EnableCaching()
public class OxoMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(OxoMainApplication.class, args);
    }
}
