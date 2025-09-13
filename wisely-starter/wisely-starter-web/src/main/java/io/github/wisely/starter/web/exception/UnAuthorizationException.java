package io.github.wisely.starter.web.exception;


import io.github.wisely.starter.core.exception.BusinessException;
import io.github.wisely.starter.core.exception.eum.CommonExceptionEnum;

import java.io.Serial;

public class UnAuthorizationException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 5487053831332234360L;

    @Override
    public int getHttpStatusCode() {
        return 401;
    }

    private UnAuthorizationException(Throwable rootCause, int code, String message, Object... objects) {
        super(rootCause, code, message, objects);
    }

    public static UnAuthorizationException of() {
        return new UnAuthorizationException(null, CommonExceptionEnum.NEED_LOGIN.getCode(), CommonExceptionEnum.NEED_LOGIN.getMessage(), (Object[]) null);
    }

}
