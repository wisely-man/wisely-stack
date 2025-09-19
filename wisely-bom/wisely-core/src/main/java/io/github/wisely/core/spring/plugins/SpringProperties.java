package io.github.wisely.core.spring.plugins;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "plugins.spring")
@Getter
@Setter
public class SpringProperties {

    private boolean enabled = true;

}
