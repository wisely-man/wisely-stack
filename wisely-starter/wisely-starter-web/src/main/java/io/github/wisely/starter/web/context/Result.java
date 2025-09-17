package io.github.wisely.starter.web.context;

import io.github.wisely.starter.core.exception.eum.CommonExceptionEnum;

import java.io.Serializable;

/**
 * 统一返回结果
 *
 * @param <T>
 */
public record Result<T> (int code, String message, long timestamp, T data) implements Serializable {


    public static <T> Result<T> response(int code, String message, T data) {
        return new Result<>(code, message, System.currentTimeMillis(), data);
    }

    public static <T> Result<T> ok(T data) {
        return response(CommonExceptionEnum.SUCCESS.getCode(), CommonExceptionEnum.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> error(int code, String message) {
        return response(code, message, null);
    }

    public static <T> Result<T> error(CommonExceptionEnum exceptionEnum) {
        return response(exceptionEnum.getCode(), exceptionEnum.getMessage(), null);
    }

}
