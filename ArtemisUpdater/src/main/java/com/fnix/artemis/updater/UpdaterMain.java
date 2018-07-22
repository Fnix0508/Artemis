package com.fnix.artemis.updater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fnix.artemis.base.BaseConfig;
import com.fnix.artemis.core.CoreConfig;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@Import({UpdaterConfig.class, CoreConfig.class, BaseConfig.class})
@EnableScheduling
public class UpdaterMain {

    public static void main(String[] args) {
        SpringApplication.run(UpdaterMain.class, args);
    }
}
