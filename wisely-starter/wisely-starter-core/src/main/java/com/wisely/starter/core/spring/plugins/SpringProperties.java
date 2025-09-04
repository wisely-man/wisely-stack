package com.wisely.starter.core.spring.plugins;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "plugin.spring")
@Getter
@Setter
public class SpringProperties {

    private boolean enabled = true;

}
