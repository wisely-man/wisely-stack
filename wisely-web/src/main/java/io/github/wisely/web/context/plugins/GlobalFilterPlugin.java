package io.github.wisely.web.context.plugins;

import io.github.wisely.core.plugin.AbstractPlugin;
import io.github.wisely.web.context.GlobalFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GlobalFilter插件
 * 将单次读取的request.getInputStream转换为可重复读的字节流
 * <p>
 * matchIfMissing = true
 * 默认加载
 * 如需关闭，请配置 plugins.filter.global.enable=false
 */
@ConditionalOnProperty(prefix = "plugins.filter.global", value = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GlobalFilterProperties.class)
public class GlobalFilterPlugin extends AbstractPlugin {


    @Override
    protected String getName() {
        return "FrameworkFilterPlugin";
    }


    @Bean
    public FilterRegistrationBean<Filter> frameworkFilterBean(GlobalFilterProperties frameworkFilterProperties) {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setBeanName("frameworkFilterBean");
        filterRegistrationBean.setFilter(new GlobalFilter());//设置为自定义的过滤器MyFilter
        filterRegistrationBean.addUrlPatterns(frameworkFilterProperties.getPatterns());//拦截所有请求
        filterRegistrationBean.setOrder(frameworkFilterProperties.getOrder());//优先级为0
        return filterRegistrationBean;
    }
}