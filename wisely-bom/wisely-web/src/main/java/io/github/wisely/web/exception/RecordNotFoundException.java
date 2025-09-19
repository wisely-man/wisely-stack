package io.github.wisely.web.exception;


import com.google.common.collect.Range;
import io.github.wisely.core.exception.BusinessException;
import io.github.wisely.core.exception.eum.CommonExceptionEnum;
import io.github.wisely.core.exception.handler.ExceptionManager;

import java.io.Serial;

/**
 * 记录未找到异常
 */
public class RecordNotFoundException extends BusinessException {

    @Serial
    private static final long serialVersionUID = -2079532394198823189L;

    static {
        ExceptionManager.addRange(Range.closed(5003, 5003), (code, message, objects) -> {
            throw RecordNotFoundException.of(objects);
        });
    }

    @Override
    public int getHttpStatusCode() {
        return 404;
    }

    public RecordNotFoundException(Throwable rootCause, int code, String message, Object... objects) {
        super(rootCause, code, message, objects);
    }

    public static RecordNotFoundException of(Object... params) {
        return new RecordNotFoundException(null, CommonExceptionEnum.RESOURCE_NOT_FOUND.getCode(), CommonExceptionEnum.RESOURCE_NOT_FOUND.getMessage(), params);
    }
}
