package io.github.wisely.web.exception.plugins;

import io.github.wisely.core.plugin.AbstractPlugin;
import io.github.wisely.web.exception.DefaultExceptionAdvice;
import io.github.wisely.web.i18n.MessageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 全局异常处理类
 */
@ConditionalOnProperty(name = "plugins.exception.enabled", havingValue = "true")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExceptionProperties.class)
public class ExceptionPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "ExceptionPlugin";
    }

    @Bean
    public DefaultExceptionAdvice defaultExceptionAdvice(@Autowired(required = false) MessageConvert messageConvert) {
        return new DefaultExceptionAdvice(messageConvert);
    }
}