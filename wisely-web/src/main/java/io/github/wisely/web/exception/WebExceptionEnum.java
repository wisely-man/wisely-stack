package io.github.wisely.web.exception;

import com.wisely.framework.core.exception.handler.FrameworkCommonAssert;
import lombok.Getter;

@Getter
public enum WebExceptionEnum implements FrameworkCommonAssert {

    // 防重复提交
    REPEAT_REQUEST(5001, "idempotent.repeat_request"),
    TOKEN_INVALID(5002, "idempotent.idempotent_key_required"),
    ;

    WebExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;

    private final String message;
}
