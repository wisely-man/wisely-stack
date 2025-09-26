package io.github.wisely.web.context;

import io.github.wisely.core.exception.eum.CommonExceptionEnum;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @param <T>
 */
public record Result<T>(int code, String message, long timestamp, T data) implements Serializable {


    /**
     * 构建返回结果
     *
     * @param code    状态码
     * @param message 消息
     * @param data    数据
     * @param <T>     数据类型
     * @return 返回结果
     */
    public static <T> Result<T> response(int code, String message, T data) {
        return new Result<>(code, message, System.currentTimeMillis(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data 数据
     * @param <T>  数据类型
     * @return 返回结果
     */
    public static <T> Result<T> ok(T data) {
        return response(CommonExceptionEnum.SUCCESS.getCode(), CommonExceptionEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param <T> 数据类型
     * @return 返回结果
     */
    public static <T> Result<T> ok() {
        return ok(null);
    }

    /**
     * 失败返回结果
     *
     * @param code    状态码
     * @param message 消息
     * @param <T>     数据类型
     * @return 返回结果
     */
    public static <T> Result<T> error(int code, String message) {
        return response(code, message, null);
    }

    /**
     * 失败返回结果
     *
     * @param exceptionEnum 异常枚举
     * @param <T>           数据类型
     * @return 返回结果
     */
    public static <T> Result<T> error(CommonExceptionEnum exceptionEnum) {
        return response(exceptionEnum.getCode(), exceptionEnum.getMessage(), null);
    }

}
