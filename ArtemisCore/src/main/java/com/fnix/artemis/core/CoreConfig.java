package com.fnix.artemis.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CoreProperties.class, RepositoryConfig.class})
@ComponentScan("com.fnix.artemis.core.service")
public class CoreConfig {

}
