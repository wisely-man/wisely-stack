package io.github.wisely.web.exception.plugins;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "plugins.exception")
@Setter
@Getter
public class ExceptionProperties {

    /**
     * 是否启用
     */
    private boolean enabled;
}
