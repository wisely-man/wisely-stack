package com.wisely.starter.core.exception.handler;


import com.wisely.starter.core.data.helper.DataHelper;
import com.wisely.starter.core.helper.RegexHelper;
import com.wisely.starter.core.helper.StringHelper;
import com.wisely.starter.core.helper.ValidHelper;
import org.springframework.lang.Contract;

import java.util.Collection;
import java.util.Map;


public interface FrameworkCommonAssert {

    int getCode();

    String getMessage();

    /**
     * false抛出異常
     * true无操作
     *
     * @param expression 表达式
     * @param params     参数
     */
    @Contract("false, _ -> fail")
    default void assertTrue(boolean expression, Object... params) {
        if (!expression) {
            ExceptionManager.buildException(getCode(), getMessage(), params);
        }
    }

    /**
     * 必定为空
     *
     * @param object 对象
     * @param params 提示参数
     */
    @Contract("!null, _ -> fail")
    default void assertNull(Object object, Object... params) {
        assertTrue(ValidHelper.isNull(object), params);
    }

    /**
     * 必定为空
     *
     * @param object 对象
     * @param params 提示参数
     */
    @Contract("null, _ -> fail")
    default void assertNotNull(Object object, Object... params) {
        assertTrue(ValidHelper.isNotNull(object), params);
    }

    /**
     * 必定为空
     *
     * @param object 对象
     * @param params 提示参数
     */
    @Contract("!null, _ -> fail")
    default void assertEmpty(Object[] object, Object... params) {
        assertTrue(ValidHelper.isEmpty(object), params);
    }


    /**
     * 必定不为空
     *
     * @param object 对象
     * @param params 提示参数
     */
    @Contract("null, _ -> fail")
    default void assertNotEmpty(Object[] object, Object... params) {
        assertTrue(ValidHelper.isNotEmpty(object), params);
    }


    /**
     * 必定为空
     *
     * @param collection 集合对象
     * @param params     提示参数
     */
    @Contract("!null, _ -> fail")
    default void assertEmpty(Collection<?> collection, Object... params) {
        assertTrue(ValidHelper.isEmpty(collection), params);
    }


    /**
     * 必定不为空
     *
     * @param collection 集合对象
     * @param params     提示参数
     */
    @Contract("null, _ -> fail")
    default void assertNotEmpty(Collection<?> collection, Object... params) {
        assertTrue(ValidHelper.isNotEmpty(collection), params);
    }


    /**
     * 必定为空
     *
     * @param map    Map对象
     * @param params 提示参数
     */
    @Contract("!null, _ -> fail")
    default void assertEmpty(Map<?, ?> map, Object... params) {
        assertTrue(ValidHelper.isEmpty(map), params);
    }

    /**
     * 必定为空
     *
     * @param map    Map对象
     * @param params 提示参数
     */
    @Contract("null, _ -> fail")
    default void assertNotEmpty(Map<?, ?> map, Object... params) {
        assertTrue(ValidHelper.isNotEmpty(map), params);
    }

    /**
     * 字符串-必定是blank
     *
     * @param object 对象
     * @param params 提示参数
     */
    @Contract("!null, _ -> fail")
    default void assertBlank(String object, Object... params) {
        assertTrue(StringHelper.isBlank(object), params);
    }

    /**
     * 字符串-必定不是blank
     *
     * @param object 对象
     * @param params 提示参数
     */
    @Contract("null, _ -> fail")
    default void assertNotBlank(String object, Object... params) {
        assertTrue(StringHelper.isNotBlank(object), params);
    }


    /**
     * 必定是指定类型
     *
     * @param object 对象
     * @param clazz  类型
     * @param params 提示参数
     */
    default void assertInstanceOf(Object object, Class<?> clazz, Object... params) {
        assertTrue(clazz.isInstance(object), params);
    }

    /**
     * 必定相等
     *
     * @param source 对象
     * @param target 比较对象
     * @param params 提示参数
     */
    default void assertEquals(Object source, Object target, Object... params) {
        assertTrue(ValidHelper.isEquals(source, target), params);
    }

    /**
     * 必定不相等
     *
     * @param source 对象
     * @param target 比较对象
     * @param params 提示参数
     */
    default void assertNotEquals(Object source, Object target, Object... params) {
        assertTrue(!ValidHelper.isEquals(source, target), params);
    }

    /**
     * 必定是数字
     *
     * @param object 对象
     * @param params 提示参数
     */
    default void assertNumber(Object object, Object... params) {
        assertTrue(RegexHelper.isNumber(DataHelper.getString(object, "")), params);
    }

    /**
     * 数组是指定大小
     *
     * @param arrays 数组
     * @param len    指定大小
     * @param params 提示参数
     */
    default void assertSize(Object[] arrays, int len, Object... params) {
        assertTrue(ValidHelper.isSize(arrays, len), params);
    }

    /**
     * 集合是指定大小
     *
     * @param collection 集合
     * @param len        指定大小
     * @param params     提示参数
     */
    default void assertSize(Collection<?> collection, int len, Object... params) {
        assertTrue(ValidHelper.isSize(collection, len), params);
    }
}
