package com.fnix.artemis.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.fnix.artemis.base.BaseConfig;
import com.fnix.artemis.core.CoreConfig;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({CoreConfig.class, BaseConfig.class, WebConfig.class, WebSecurityConfig.class})
public class WebMain {

    public static void main(String[] args) {
        SpringApplication.run(WebMain.class, args);
    }
}
