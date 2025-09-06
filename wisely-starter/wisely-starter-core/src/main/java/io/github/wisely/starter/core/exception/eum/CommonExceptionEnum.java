package io.github.wisely.starter.core.exception.eum;

import io.github.wisely.starter.core.exception.handler.FrameworkCommonAssert;
import lombok.Getter;

/**
 *
 */
@Getter
public enum CommonExceptionEnum implements FrameworkCommonAssert {

    SUCCESS(0, "success"),
    SYSTEM(1000, "common.system_failed"),
    VALIDATION(2000, "common.validation_failed"),
    THIRD_PARTY(3000, "common.third_party_failed"),
    BUSINESS(5000, "common.business_failed"),
    FAIL(9999, "common.base_failed"),

    // SystemException 1000-1999
    CONFIG_ERROR(1001, "common.config_error"),
    UNREACHABLE_CODE(1002, "common.unreachable_code"),

    // ValidationException 2000-2999
    PARAMETER_REQUIRED(2001, "common.parameter_required"),
    PARAMETER_INVALID(2002, "common.parameter_invalid"),

    // ThirdPartyException 3000-3999
    RESPONSE_FAILED(3001, "common.response_failed"),

    // BusinessException 5000-5999
    NEED_LOGIN(5001, "common.login_required"),
    OPERATOR_FORBIDDEN(5002, "common.operator_forbidden"),
    RESOURCE_NOT_FOUND(5003, "common.resource_not_found"),
    ;

    CommonExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private final int code;
    private final String message;

}
