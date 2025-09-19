package io.github.wisely.core.thread.plugins;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

@ConfigurationProperties(prefix = "plugins.async")
@Setter
@Getter
public class AsyncProperties {

    /**
     * 是否开启
     */
    private boolean enabled = false;

    /**
     * 线程模式，virtual-虚拟线程，system-系统线程，默认：virtual
     */
    private ModeEnum mode = ModeEnum.VIRTUAL;

    /**
     * 虚拟线程
     */
    private VirtualThread virtual = new VirtualThread();

    /**
     * 系统线程
     */
    private SystemThread system = new SystemThread();

    @Setter
    @Getter
    public static class VirtualThread {
        String threadNamePrefix = "virtual-thread-";
    }

    @Setter
    @Getter
    public static class SystemThread {
        /**
         * 核心线程数
         */
        private Integer corePoolSize = 5;

        /**
         * 最大线程数
         */
        private Integer maxPoolSize = 20;

        /**
         * 最大队列长度
         */
        private Integer queueCapacity = 200;

        /**
         * 线程池中线程最大空闲时间，默认：60，单位：秒
         */
        private Integer KeepAliveSeconds = 60;

        /**
         * 核心线程是否允许超时，默认:false
         */
        private boolean allowCoreThreadTimeOut = false;

        /**
         * IOC容器关闭时是否阻塞等待剩余的任务执行完成，默认:false（必须设置awaitTerminationSeconds）
         */
        private boolean waitForTasksToCompleteOnShutdown = false;

        /**
         * IOC容器关闭时阻塞时间，默认：10，单位：秒
         */
        private Integer awaitTerminationSeconds = 10;

        /**
         * 线程名称前缀
         */
        private String threadNamePrefix = "async-thread-";

        /**
         * 拒绝策略，默认是AbortPolicy
         * AbortPolicy：丢弃任务并抛出RejectedExecutionException异常
         * DiscardPolicy：丢弃任务但不抛出异常
         * DiscardOldestPolicy：丢弃最旧的处理程序，然后重试，如果执行器关闭，这时丢弃任务
         * CallerRunsPolicy：执行器执行任务失败，则在策略回调方法中执行任务，如果执行器关闭，这时丢弃任务
         */
        private String rejectedExecutionHandler = "AbortPolicy";

        /**
         * 根据配置获取拒绝策略，默认AbortPolicy
         *
         * @return 拒绝策略
         */
        public RejectedExecutionHandler getRejectedExecution() {
            return RejectExecutionEnum.getRejectedExecution(this.rejectedExecutionHandler);
        }

        @Getter
        enum RejectExecutionEnum {
            ABORT_POLICY("AbortPolicy", new ThreadPoolExecutor.AbortPolicy()),
            DISCARD_POLICY("DiscardPolicy", new ThreadPoolExecutor.DiscardPolicy()),
            DISCARD_OLDEST_POLICY("DiscardOldestPolicy", new ThreadPoolExecutor.DiscardOldestPolicy()),
            CALLER_RUNS_POLICY("CallerRunsPolicy", new ThreadPoolExecutor.CallerRunsPolicy()),
            ;

            RejectExecutionEnum(String name, RejectedExecutionHandler rejectedExecutionHandler) {
                this.name = name;
                this.rejectedExecutionHandler = rejectedExecutionHandler;
            }

            private final String name;
            private final RejectedExecutionHandler rejectedExecutionHandler;


            private static final Map<String, RejectedExecutionHandler> MODEL = Maps.newHashMap();

            static {
                Stream.of(RejectExecutionEnum.values())
                        .forEach(eum -> MODEL.put(eum.name, eum.rejectedExecutionHandler));
            }

            public static RejectedExecutionHandler getRejectedExecution(String name) {
                return MODEL.entrySet().stream()
                        .filter(e -> e.getKey().equalsIgnoreCase(name))
                        .map(Map.Entry::getValue)
                        .findFirst()
                        .orElse(ABORT_POLICY.getRejectedExecutionHandler());
            }
        }
    }


    @Getter
    enum ModeEnum {
        /**
         * 虚拟线程
         */
        VIRTUAL("virtual"),
        /**
         * 系统线程
         */
        SYSTEM("system"),
        ;

        ModeEnum(String name) {
            this.name = name;
        }

        private final String name;
    }

}
