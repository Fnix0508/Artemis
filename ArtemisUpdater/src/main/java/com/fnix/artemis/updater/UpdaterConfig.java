package com.fnix.artemis.updater;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import com.fnix.artemis.base.brain.ArtemisBrain;
import com.fnix.artemis.base.brain.ArtemisBrainFactoryBean;

@Configuration
@Import({UpdaterProperties.class})
public class UpdaterConfig {

    @Autowired
    private UpdaterProperties updaterProperties;

    @Bean
    public ArtemisBrain artemisBrain(ArtemisBrainFactoryBean artemisBrainFactoryBean) {
        return artemisBrainFactoryBean.create();
    }

    @Bean
    public ExecutorService systemExecutor() {
        return Executors.newFixedThreadPool(updaterProperties.getSystemThreadNum(), new CustomizableThreadFactory("system-"));
    }

    @Bean
    public ExecutorService subExecutor() {
        return Executors.newFixedThreadPool(updaterProperties.getSystemThreadNum(), new CustomizableThreadFactory("sub-"));
    }
}
