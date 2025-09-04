package com.wisely.starter.core.data.plugins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wisely.starter.core.data.helper.JsonHelper;
import com.wisely.starter.core.plugin.AbstractPlugin;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;


@ConditionalOnProperty(prefix = "plugins.infrastructure", name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(InfrastructureProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class InfrastructurePlugin extends AbstractPlugin implements Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected String getName() {
        return "InfrastructurePlugin";
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return JsonHelper.FrameworkObjectMapper.INSTANCE;
    }

}
