package com.wisely.starter.core.spring.plugins;

import com.wisely.starter.core.spring.helper.ConfigHelper;
import com.wisely.starter.core.spring.helper.SpringHelper;
import jakarta.annotation.Nonnull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Spring 相关Helper应用
 */
@ConditionalOnProperty(prefix = "plugin.spring", name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SpringProperties.class)
public class SpringPlugin implements ApplicationContextAware, EnvironmentAware {

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        ConfigHelper.setEnvironment(environment);
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) {
        SpringHelper.setApplicationContext(applicationContext);
    }
}
