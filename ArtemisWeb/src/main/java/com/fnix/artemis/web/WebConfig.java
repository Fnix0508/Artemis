package com.fnix.artemis.web;

import org.springframework.context.annotation.Bean;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import com.fnix.artemis.base.brain.ArtemisBrain;
import com.fnix.artemis.base.brain.ArtemisBrainFactoryBean;

@EnableJdbcHttpSession
public class WebConfig {

    @Bean
    public ArtemisBrain artemisBrain(ArtemisBrainFactoryBean artemisBrainFactoryBean) {
        return artemisBrainFactoryBean.create();
    }
}
