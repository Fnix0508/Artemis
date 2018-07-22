package com.fnix.artemis.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "core.system")
public class CoreProperties {

    private String networkCode = "test";
}

