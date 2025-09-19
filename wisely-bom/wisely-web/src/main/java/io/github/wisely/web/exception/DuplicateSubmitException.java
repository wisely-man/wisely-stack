package io.github.wisely.web.exception;


import com.google.common.collect.Range;
import io.github.wisely.core.exception.BusinessException;
import io.github.wisely.core.exception.handler.ExceptionManager;

import java.io.Serial;

public class DuplicateSubmitException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -6464444556286611370L;

    @Override
    public int getHttpStatusCode() {
        return 409;
    }

    static {
        // 注册重复提交code码
        ExceptionManager.addRange(Range.closed(5001, 5002), (code, message, objects) -> {
            throw DuplicateSubmitException.of(code, message, objects);
        });
    }

    public DuplicateSubmitException(Throwable rootCause, int code, String message, Object... objects) {
        super(rootCause, code, message, objects);
    }

    public static DuplicateSubmitException of(int code, String message, Object... objects) {
        return new DuplicateSubmitException(null, code, message, objects);
    }
}
