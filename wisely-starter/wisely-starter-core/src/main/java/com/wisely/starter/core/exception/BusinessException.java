package com.wisely.starter.core.exception;


import com.wisely.starter.core.exception.eum.CommonExceptionEnum;

import java.io.Serial;

/**
 * 业务异常
 */
public class BusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = 5268382542901221287L;

    @Override
    public int getHttpStatusCode() {
        return 400;
    }

    public BusinessException(Throwable rootCause, int code, String message, Object... objects) {
        super(rootCause, code, message, objects);
    }

    public static BusinessException of() {
        return new BusinessException(null, CommonExceptionEnum.BUSINESS.getCode(),  CommonExceptionEnum.BUSINESS.getMessage(), (Object[]) null);
    }

    public static BusinessException of(int code, String message, Object... objects) {
        return new BusinessException(null, code, message, objects);
    }

    public static BusinessException of(int code, String message) {
        return new BusinessException(null, code, message, (Object[]) null);
    }

    public static BusinessException of(String message, Object... objects) {
        return new BusinessException(null, CommonExceptionEnum.BUSINESS.getCode(), message, objects);
    }

    public static BusinessException of(Throwable rootCause, String message, Object... objects) {
        return new BusinessException(rootCause, CommonExceptionEnum.BUSINESS.getCode(), message, objects);
    }
}
