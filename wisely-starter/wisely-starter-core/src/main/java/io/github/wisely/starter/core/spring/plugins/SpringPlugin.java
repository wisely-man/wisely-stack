package io.github.wisely.starter.core.spring.plugins;

import io.github.wisely.starter.core.plugin.AbstractPlugin;
import io.github.wisely.starter.core.spring.helper.ConfigHelper;
import io.github.wisely.starter.core.spring.helper.SpringHelper;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;

/**
 * Spring 相关Helper应用
 */
@ConditionalOnProperty(prefix = "plugins.spring", name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SpringProperties.class)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class SpringPlugin extends AbstractPlugin implements ApplicationContextAware, EnvironmentAware {

    @Override
    protected String getName() {
        return "SpringPlugin";
    }

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        ConfigHelper.setEnvironment(environment);
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) {
        SpringHelper.setApplicationContext(applicationContext);
    }
}
