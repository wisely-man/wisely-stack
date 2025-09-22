package io.github.wisely.web.i18n.plugins;

import io.github.wisely.core.helper.StringHelper;
import io.github.wisely.core.plugin.AbstractPlugin;
import io.github.wisely.web.i18n.DefaultI18nMessageConvert;
import io.github.wisely.web.i18n.FrameworkMessageSource;
import io.github.wisely.web.i18n.MessageConvert;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Arrays;

/**
 * 国际化插件
 */
@ConditionalOnProperty(prefix = "plugins.i18n", name = "enabled", havingValue = "true")
@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(I18nProperties.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 9)
public class I18nPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "i18nPlugin";
    }

    /**
     * 未定义消息转换器，启用国际化消息转换器
     *
     * @return 默认消息转换器
     */
    @Bean
    @ConditionalOnMissingBean(MessageConvert.class)
    public MessageConvert messageConvert() {
        return new DefaultI18nMessageConvert();
    }


    @Bean
    public MessageSource messageSource(I18nProperties i18nProperties) {
        ReloadableResourceBundleMessageSource messageSource = new FrameworkMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        String[] resources = StringHelper.split(i18nProperties.getBaseNames(), ",");
        log.trace("message resources : {}", Arrays.toString(resources));
        messageSource.setBasenames(resources);
        messageSource.setDefaultEncoding(i18nProperties.getEncoding());
        return messageSource;
    }


    @Bean
    public LocaleResolver localeResolver(I18nProperties i18nProperties) {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(i18nProperties.getI18nLocale());
        return localeResolver;
    }


    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor(I18nProperties i18nProperties) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(i18nProperties.getLocaleKey());
        return localeChangeInterceptor;
    }

    @Bean
    public WebMvcConfigurer localeInterceptor(LocaleChangeInterceptor localeChangeInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(@Nonnull InterceptorRegistry registry) {
                registry.addInterceptor(localeChangeInterceptor);
            }
        };
    }


}
