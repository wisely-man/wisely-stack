package io.github.wisely.starter.core.exception;


import io.github.wisely.starter.core.exception.eum.CommonExceptionEnum;

import java.io.Serial;

/**
 * 系统异常
 */
public class SystemException extends BaseException {

    @Serial
    private static final long serialVersionUID = 8171676489082518688L;

    @Override
    public int getHttpStatusCode() {
        return 500;
    }

    public SystemException(Throwable rootCause, int code, String message, Object... objects) {
        super(rootCause, code, message, objects);
    }

    public static SystemException of() {
        return new SystemException(null, CommonExceptionEnum.SYSTEM.getCode(), CommonExceptionEnum.SYSTEM.getMessage(), (Object[]) null);
    }

    public static SystemException of(Throwable throwable, int code, String message, Object... objects) {
        return new SystemException(throwable, code, message, objects);
    }

    public static SystemException of(int code, String message, Object... objects) {
        return new SystemException(null, code, message, objects);
    }

    public static SystemException of(String message, Object... objects) {
        return new SystemException(null, CommonExceptionEnum.SYSTEM.getCode(), message, objects);
    }

    public static SystemException of(Throwable rootCause, String message, Object... objects) {
        return new SystemException(rootCause, CommonExceptionEnum.SYSTEM.getCode(), message, objects);
    }

}
