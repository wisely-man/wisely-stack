package io.github.wisely.web.context.plugins;

import io.github.wisely.core.plugin.AbstractPlugin;
import io.github.wisely.web.context.FrameworkFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * FrameworkFilter插件
 * 将单次读取的request.getInputStream转换为可重复读的字节流
 * <p>
 * matchIfMissing = true
 * 默认加载
 * 如需关闭，请配置 plugins.filter.framework.enable=false
 */
@ConditionalOnProperty(prefix = "plugins.filter.framework", value = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(FrameworkFilterProperties.class)
public class FrameworkFilterPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "FrameworkFilterPlugin";
    }

    @Bean
    public FrameworkFilter frameworkFilter() {
        return new FrameworkFilter();
    }

    @Bean
    public FilterRegistrationBean<Filter> frameworkFilterBean(FrameworkFilterProperties frameworkFilterProperties, FrameworkFilter filter) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(filter);//设置为自定义的过滤器MyFilter
        filterRegistrationBean.addUrlPatterns(frameworkFilterProperties.getPatterns());//拦截所有请求
        filterRegistrationBean.setOrder(frameworkFilterProperties.getOrder());//优先级为0
        return filterRegistrationBean;
    }
}