package io.github.wisely.core.thread.plugins;

import io.github.wisely.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.core.exception.handler.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步任务线程池
 */
@ConditionalOnProperty(prefix = "plugins.async", value = "enabled", havingValue = "true")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AsyncProperties.class)
@EnableAsync
@Slf4j
public class AsyncPlugin implements AsyncConfigurer {

    // 直接注入
    private final AsyncProperties properties;

    public AsyncPlugin(AsyncProperties properties) {
        this.properties = properties;
    }


    @Bean("asyncTaskExecutor")
    @Override
    public Executor getAsyncExecutor() {

        if (AsyncProperties.ModeEnum.VIRTUAL.equals(properties.getMode())) {
            // Spring 6.1+ 才有 setVirtualThreads(true)
            SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
            executor.setVirtualThreads(true);        // 关键：使用虚拟线程
            executor.setThreadNamePrefix(properties.getVirtual().getThreadNamePrefix());
            // 其余 core/max/queue 等参数对虚拟线程无效，直接忽略
            return executor;
        }

        if (AsyncProperties.ModeEnum.SYSTEM.equals(properties.getMode())) {
            AsyncProperties.SystemThread systemConfig = properties.getSystem();
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(systemConfig.getCorePoolSize()); // 核心线程数
            executor.setMaxPoolSize(systemConfig.getMaxPoolSize()); // 最大线程数
            executor.setQueueCapacity(systemConfig.getQueueCapacity()); // 队列大小
            executor.setThreadNamePrefix(systemConfig.getThreadNamePrefix()); // 线程名称
            executor.setRejectedExecutionHandler(systemConfig.getRejectedExecution()); // 拒绝策略
            // 线程池中线程最大空闲时间
            executor.setKeepAliveSeconds(systemConfig.getKeepAliveSeconds());
            // 核心线程是否允许超时
            executor.setAllowCoreThreadTimeOut(systemConfig.isAllowCoreThreadTimeOut());
            // IOC容器关闭时是否阻塞等待剩余的任务执行完成，默认:false（必须设置setAwaitTerminationSeconds）
            executor.setWaitForTasksToCompleteOnShutdown(systemConfig.isWaitForTasksToCompleteOnShutdown());
            // 阻塞IOC容器关闭的时间，默认：10秒（必须设置setWaitForTasksToCompleteOnShutdown）
            executor.setAwaitTerminationSeconds(systemConfig.getAwaitTerminationSeconds());
            // 初始化
            executor.initialize();
            return executor;
        }

        throw ExceptionManager.buildException(CommonExceptionEnum.CONFIG_ERROR, "plugins.async.mode");
    }


    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
