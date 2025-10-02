package io.github.wisely.web.http.plugins;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@ConfigurationProperties(prefix = "plugins.http")
@Setter
@Getter
public class HttpProperties {

    /**
     * 是否开启远程工具
     */
    private boolean enabled;

    /**
     * 编解码内存缓存大小，默认2MB，超过该大小则启用临时文件缓存
     */
    private int maxInMemorySize = 2 * 1024 * 1024; // 2MB

    /**
     * 连接池配置
     */
    private Pool pool = new Pool();

    /**
     * 客户端配置
     */
    private Client client = new Client();


    @Setter
    @Getter
    static class Pool {
        /**
         * 最大连接数
         */
        private int maxConnections = 100;
        /**
         * 连接最大空闲时间，默认值60秒
         *
         */
        private Duration maxIdleTime = Duration.ofSeconds(60);
        /**
         * 连接最长存活时间，默认值5分钟
         */
        private Duration maxLifeTime = Duration.ofMinutes(5);
        /**
         * 获取连接超时，默认值3000毫秒
         */
        private Duration pendingAcquireTimeout = Duration.ofMillis(3000);
    }


    @Setter
    @Getter
    static class Client {
        /**
         * 连接超时时间，单位毫秒，默认 10000 毫秒（10 秒）
         */
        private Duration connectTimeout = Duration.ofMillis(10_000);
        /**
         * 是否启用 TCP Keep-Alive，默认 true
         */
        private boolean soKeepalive = true;
        /**
         * 是否禁用 Nagle 算法（低延迟），默认 true
         */
        private boolean tcpNoDelay = true;
        /**
         * 是否允许地址重用，默认 true
         */
        private boolean soReuseaddr = true;
        /**
         * 读超时时间，单位秒，默认 10 秒
         */
        private int readTimeout = 10;
        /**
         * 写超时时间，单位秒，默认 10 秒
         */
        private int writeTimeout = 10;
        /**
         * 从发送请求到收到响应头的总超时，默认 10 秒
         */
        private Duration responseTimeout = Duration.ofSeconds(10);
        /**
         * 是否启用 GZIP 压缩，默认 true
         */
        private boolean compress = true;
    }
}
