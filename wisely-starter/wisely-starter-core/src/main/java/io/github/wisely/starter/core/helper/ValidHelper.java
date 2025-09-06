package io.github.wisely.starter.core.helper;


import org.springframework.lang.Contract;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * 验证工具类
 * <p>
 * 提供常用的空值判断、非空判断、集合大小、正则匹配、对象比较等方法。
 * 所有方法均为静态无状态，线程安全。
 * </p>
 */
public class ValidHelper {

    /**
     * 工具类禁止实例化
     */
    private ValidHelper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // ======================================
    // =           空值判断
    // ======================================

    /**
     * 判断对象是否为 null
     *
     * @param object 待判断对象
     * @return true if null, false otherwise
     */
    @Contract("null -> true; !null -> false")
    public static boolean isNull(@Nullable Object object) {
        return object == null;
    }

    /**
     * 判断对象是否非 null
     *
     * @param object 待判断对象
     * @return true if not null, false otherwise
     */
    @Contract("null -> false; !null -> true")
    public static boolean isNotNull(@Nullable Object object) {
        return !isNull(object);
    }

    /**
     * 判断数组对象是否为 null 或 空
     *
     * @param array 待判断对象
     * @return true if null or empty, false otherwise
     */
    @Contract("null -> true")
    public static boolean isEmpty(@Nullable Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 判断数组对象是否不为 null 或 空
     *
     * @param array 待判断对象
     * @return true if null or empty, false otherwise
     */
    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断Map对象是否为 null 或 空
     *
     * @param map 待判断对象
     * @return true if null or empty, false otherwise
     */
    @Contract("null -> true")
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 判断Map对象是否不为 null 或 空
     *
     * @param map 待判断对象
     * @return true if null or empty, false otherwise
     */
    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断集合对象是否为 null 或 空
     *
     * @param collection 待判断对象
     * @return true if null or empty, false otherwise
     */
    @Contract("null -> true")
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合对象是否不为 null 或 空
     *
     * @param collection 待判断对象
     * @return true if null or empty, false otherwise
     */
    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }


    // ======================================
    // =           集合/数组大小判断
    // ======================================

    /**
     * 判断数组长度是否等于指定值
     *
     * @param array 数组
     * @param len   指定长度
     * @return true if size equals len
     */
    public static boolean isSize(Object[] array, int len) {
        if (len == 0) {
            return array == null || isEmpty(array);
        }
        return isNotEmpty(array) && array.length == len;
    }

    /**
     * 判断集合大小是否等于指定值
     *
     * @param collection 集合
     * @param len        指定长度
     * @return true if size equals len
     */
    public static boolean isSize(Collection<?> collection, int len) {
        if (len == 0) {
            return collection == null || isEmpty(collection);
        }
        return isNotEmpty(collection) && collection.size() == len;
    }

    // ======================================
    // =           相等判断
    // ======================================

    /**
     * 判断两个对象是否相等
     * 使用 Objects.equals，安全处理 null
     */
    public static boolean isEquals(Object o1, Object o2) {
        return Objects.equals(o1, o2);
    }

    /**
     * 判断两个对象是否不相等
     */
    public static boolean isNotEquals(Object o1, Object o2) {
        return !isEquals(o1, o2);
    }


    // ======================================
    // =           包含判断
    // ======================================

    /**
     * 判断值是否在给定的选项中
     *
     * @param matches 选项数组
     * @param value   待判断值
     * @return true if value equals any match
     */
    public static <T> boolean contains(T[] matches, T value) {
        if (isEmpty(matches)) {
            return false;
        }
        for (T match : matches) {
            if (isEquals(value, match)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断值是否在给定的集合中
     *
     * @param collection 选项集合
     * @param value      待判断值
     * @return true if value equals any element in collection
     */
    public static <T> boolean contains(Collection<T> collection, T value) {
        return collection != null && collection.contains(value);
    }
}