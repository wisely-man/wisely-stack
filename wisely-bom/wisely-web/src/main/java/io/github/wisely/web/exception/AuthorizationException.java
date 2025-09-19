package io.github.wisely.web.exception;

import io.github.wisely.core.exception.BusinessException;
import io.github.wisely.core.exception.eum.CommonExceptionEnum;

import java.io.Serial;

public class AuthorizationException extends BusinessException {

    @Serial
    private static final long serialVersionUID = 5487053831332234360L;

//    static {
//        // 注册权限异常
//        ExceptionManager.addRange(, ), (code, message, objects) -> {
//            throw AuthorizationException.of();
//        });
//    }

    @Override
    public int getHttpStatusCode() {
        return 403;
    }

    private AuthorizationException(Throwable rootCause, int code, String message, Object... objects) {
        super(rootCause, code, message, objects);
    }

    public static AuthorizationException of() {
        return new AuthorizationException(null, CommonExceptionEnum.OPERATOR_FORBIDDEN.getCode(), CommonExceptionEnum.OPERATOR_FORBIDDEN.getMessage(), (Object[]) null);
    }

}
