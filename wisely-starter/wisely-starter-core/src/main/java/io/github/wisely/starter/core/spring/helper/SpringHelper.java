package io.github.wisely.starter.core.spring.helper;

import io.github.wisely.starter.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.starter.core.helper.ValidHelper;
import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * Spring工具类
 */
@UtilityClass
@Slf4j
public class SpringHelper {

    public static void setApplicationContext(@Nonnull ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
        log.info("Spring Context自动初始化完成，可以通过SpringHelper类提供的方法来获取Bean.");
    }

    private static ApplicationContext context = null;


    public static ApplicationContext getInstance() {
        CommonExceptionEnum.CONFIG_ERROR.assertNotNull(context, "ApplicationContext");
        return context;
    }


    /**
     * 检查是否有指定bean
     *
     * @param name Bean名称
     * @return true-有，false-无
     */
    public static boolean hasBean(String name) {
        return context != null && context.containsBean(name);
    }


    /**
     * 检查是否有指定bean
     *
     * @param cls Bean类
     * @return true-有，false-无
     */
    public static boolean hasBean(@Nonnull Class<?> cls) {
        return context != null && ValidHelper.isNotEmpty(context.getBeansOfType(cls));
    }

    /**
     * 根据类型获取Bean
     *
     * @param cls Bean类
     * @param <T> Bean类型
     * @return Bean对象
     */
    public static <T> T getBean(Class<T> cls) {
        return !hasBean(cls) ? null : context.getBean(cls);
    }

    /**
     * 根据名称获取Bean
     *
     * @param name Bean名称
     * @return Bean对象
     */
    public static Object getBean(String name) {
        return !hasBean(name) ? null : context.getBean(name);
    }

    /**
     * 根据Bean名称和类获取Bean对象
     *
     * @param name Bean名称
     * @param cls  Bean类
     * @param <T>  Bean类型
     * @return Bean对象
     */
    public static <T> T getBean(String name, Class<T> cls) {
        return !hasBean(name) ? null : context.getBean(name, cls);
    }

}
