package com.wisely.starter.core.exception;


import com.wisely.starter.core.exception.eum.CommonExceptionEnum;

import java.io.Serial;

/**
 * 第三方异常
 */
public class ThirdPartyException extends BaseException {

    @Serial
    private static final long serialVersionUID = 3422989564563982408L;

    @Override
    public int getHttpStatusCode() {
        return 502;
    }

    public ThirdPartyException(Throwable rootCause, int code, String message, Object... objects) {
        super(rootCause, code, message, objects);
    }

    public static ThirdPartyException of() {
        return new ThirdPartyException(null, CommonExceptionEnum.THIRD_PARTY.getCode(), CommonExceptionEnum.THIRD_PARTY.getMessage(), (Object[]) null);
    }

    public static ThirdPartyException of(int code, String message, Object... objects) {
        return new ThirdPartyException(null, code, message, objects);
    }

    public static ThirdPartyException of(int code, String message) {
        return new ThirdPartyException(null, code, message, (Object[]) null);
    }

    public static ThirdPartyException of(String message, Object... objects) {
        return new ThirdPartyException(null, CommonExceptionEnum.THIRD_PARTY.getCode(), message, objects);
    }

    public static ThirdPartyException of(Throwable rootCause, String message, Object... objects) {
        return new ThirdPartyException(rootCause, CommonExceptionEnum.THIRD_PARTY.getCode(), message, objects);
    }

}
