package io.github.wisely.starter.core.exception;


import io.github.wisely.starter.core.exception.eum.CommonExceptionEnum;

import java.io.Serial;

/**
 * 参数验证类异常
 */
public class ValidationException extends BaseException {

    @Serial
    private static final long serialVersionUID = 5323538471706886472L;

    @Override
    public int getHttpStatusCode() {
        return 422;
    }

    public ValidationException(Throwable rootCause, int code, String message, Object... objects) {
        super(rootCause, code, message, objects);
    }

    public static ValidationException of() {
        return new ValidationException(null, CommonExceptionEnum.VALIDATION.getCode(), CommonExceptionEnum.VALIDATION.getMessage(), (Object[]) null);
    }

    public static ValidationException of(int code, String message, Object... objects) {
        return new ValidationException(null, code, message, objects);
    }

    public static ValidationException of(int code, String message) {
        return new ValidationException(null, code, message, (Object[]) null);
    }

    public static ValidationException of(String message, Object... objects) {
        return new ValidationException(null, CommonExceptionEnum.VALIDATION.getCode(), message, objects);
    }

    public static ValidationException of(Throwable rootCause, String message, Object... objects) {
        return new ValidationException(rootCause, CommonExceptionEnum.VALIDATION.getCode(), message, objects);
    }
}
