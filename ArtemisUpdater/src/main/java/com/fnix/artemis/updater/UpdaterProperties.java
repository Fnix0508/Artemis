package com.fnix.artemis.updater;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "updater.property")
public class UpdaterProperties {

    private int systemThreadNum = 10;

    private int subThreadNum = 50;
}
