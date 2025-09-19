package io.github.wisely.core.data.eum;

import lombok.Getter;

@Getter
public enum Common {

    /**
     * 标准日期时间：2025-04-05 14:30:25
     */
    DATETIME("yyyy-MM-dd HH:mm:ss"),

    /**
     * 带斜杠的日期时间（斜杠分隔）：2025/04/05 14:30:25
     */
    DATETIME_SLASH("yyyy/MM/dd HH:mm:ss"),

    /**
     * 标准日期时间（到毫秒）：2025-04-05 14:30:25.111
     */
    DATETIME_MILLIS("yyyy-MM-dd HH:mm:ss.SSS"),

    /**
     * 带斜杠的日期时间（到毫秒）：2025/04/05 14:30:25.111
     */
    DATETIME_SLASH_MILLIS("yyyy/MM/dd HH:mm:ss.SSS"),

    /**
     * 标准日期时间（到分钟）：2025-04-05 14:30
     */
    DATETIME_MIN("yyyy-MM-dd HH:mm"),

    /**
     * 带斜杠的日期时间（到分钟）：2025/04/05 14:30
     */
    DATETIME_SLASH_MIN("yyyy/MM/dd HH:mm"),

    /**
     * 标准日期：2025-04-05
     */
    DATE("yyyy-MM-dd"),

    /**
     * 带斜杠的日期：2025/04/05
     */
    DATE_SLASH("yyyy-MM-dd"),

    /**
     * 标准时间：14:30:25
     */
    TIME("HH:mm:ss"),
    ;

    Common(String pattern) {
        this.pattern = pattern;
    }

    private final String pattern;

}
