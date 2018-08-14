package com.fnix.artemis.base;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.fnix.artemis.base.brain.ArtemisBrainFactoryBean;
import com.fnix.artemis.base.match.MatchPlayService;

@Configuration
@ComponentScan(basePackageClasses = {ArtemisBrainFactoryBean.class, MatchPlayService.class})
public class BaseConfig {
}
