package io.github.wisely.web.context.plugins;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;


@ConfigurationProperties(prefix = "plugins.filter.global")
@Setter
@Getter
public class GlobalFilterProperties {

    /**
     * 是否开启
     * 默认开启frameworkFilter
     */
    private boolean enabled = true;

    /**
     * 过滤请求
     */
    private String[] patterns = {"/*"};

    /**
     * filter排序
     */
    private Integer order = Ordered.HIGHEST_PRECEDENCE;
}
