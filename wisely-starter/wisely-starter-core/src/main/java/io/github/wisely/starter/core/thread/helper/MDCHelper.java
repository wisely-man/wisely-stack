package io.github.wisely.starter.core.thread.helper;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.util.Map;

/**
 * MDC线程包装类
 *    实现wisely-tract-id的跨线程传递
 */
@UtilityClass
public class MDCHelper {

    public static Runnable wrap(Runnable runnable) {
        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            if (context != null) {
                context.forEach(MDC::put);
            }
            try {
                runnable.run();
            } finally {
                if (context != null) {
                    context.forEach((k, v) -> MDC.remove(k));
                }
            }
        };
    }
}
