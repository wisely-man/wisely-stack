package io.github.wisely.web.exception;

import com.google.common.collect.Range;
import io.github.wisely.core.exception.BusinessException;
import io.github.wisely.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.core.exception.handler.ExceptionManager;

import java.io.Serial;

public class UnAuthorizationException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 5487053831332234360L;

    static {
        ExceptionManager.addRange(Range.closed(5001, 5001), UnAuthorizationException::of);
    }

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
