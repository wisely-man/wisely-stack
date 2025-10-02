package io.github.wisely.web.http.plugins;

import io.github.wisely.core.plugin.AbstractPlugin;
import io.github.wisely.web.http.WebClientFilter;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

/**
 * 网络通讯插件
 */
@ConditionalOnProperty(prefix = "plugins.http", value = "enabled", havingValue = "true")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HttpProperties.class)
@Slf4j
public class HttpPlugin extends AbstractPlugin {

    @Override
    protected String getName() {
        return "HttpPlugin";
    }


    @Bean
    @ConditionalOnMissingBean(ConnectionProvider.class)
    public ConnectionProvider connectionProvider(HttpProperties props) {
        HttpProperties.Pool p = props.getPool();
        return ConnectionProvider.builder("netty-client-conn")
                .maxConnections(p.getMaxConnections())
                .maxIdleTime(p.getMaxIdleTime())
                .maxLifeTime(p.getMaxLifeTime())
                .pendingAcquireTimeout(p.getPendingAcquireTimeout())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient(HttpProperties props, ConnectionProvider provider) {
        HttpProperties.Client c = props.getClient();
        return HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) c.getConnectTimeout().toMillis())
                .option(ChannelOption.SO_KEEPALIVE, c.isSoKeepalive())
                .option(ChannelOption.TCP_NODELAY, c.isTcpNoDelay())
                .option(ChannelOption.SO_REUSEADDR, c.isSoReuseaddr())
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(c.getReadTimeout()))
                        .addHandlerLast(new WriteTimeoutHandler(c.getWriteTimeout())))
                .responseTimeout(c.getResponseTimeout())
                .compress(c.isCompress());
    }


    @Bean
    @ConditionalOnMissingBean(WebClientFilter.class)
    public WebClientFilter webClientFilter() {
        return new WebClientFilter();
    }


    @Bean
    @ConditionalOnMissingBean(WebClient.class)
    public WebClient webClient(HttpClient httpClient,
                               HttpProperties props,
                               ObjectProvider<WebClientFilter> filters,
                               ObjectProvider<WebClientCustomizer> customizers) {
        WebClient.Builder builder =
                WebClient.builder()
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
                        .codecs(c -> c.defaultCodecs()
                                .maxInMemorySize(props.getMaxInMemorySize()));
        // 添加过滤器
        filters.orderedStream().forEach(builder::filter);
        // 添加自定义配置
        customizers.orderedStream().forEach(c -> c.customize(builder));
        return builder.build();
    }
}
