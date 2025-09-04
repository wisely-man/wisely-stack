package com.wisely.starter.core.data.eum;

import lombok.Getter;

@Getter
public enum Compact {
    /**
     * 紧凑时间：20250405143025
     */
    DATETIME("yyyyMMddHHmmss"),

    /**
     * 紧凑时间（到分钟）：202504051430
     */
    DATETIME_MIN("yyyyMMddHHmm"),

    /**
     * 紧凑时间（到毫秒）：20250405143025111
     */
    DATETIME_MILLIS("yyyyMMddHHmmssSSS"),

    /**
     * 紧凑日期：20250405
     */
    DATE("yyyyMMdd"),

    /**
     * 紧凑时间（时分秒）：143025
     */
    TIME("HHmmss");

    Compact(String pattern) {
        this.pattern = pattern;
    }

    private final String pattern;
}
