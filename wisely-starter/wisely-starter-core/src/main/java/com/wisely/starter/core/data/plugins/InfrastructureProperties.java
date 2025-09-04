package com.wisely.starter.core.data.plugins;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "plugin.infrastructure")
@Setter
@Getter
public class InfrastructureProperties {

    private boolean enabled = true;

}
