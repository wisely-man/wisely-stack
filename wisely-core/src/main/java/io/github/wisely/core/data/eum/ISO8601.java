package io.github.wisely.core.data.eum;

import lombok.Getter;

@Getter
public enum ISO8601 {

    /**
     * 基础 ISO：2025-04-05T14:30:25
     */
    BASIC("yyyy-MM-dd'T'HH:mm:ss"),

    /**
     * 带毫秒：2025-04-05T14:30:25.123
     */
    MS("yyyy-MM-dd'T'HH:mm:ss.SSS"),

    /**
     * 带时区偏移：2025-04-05T14:30:25+0800
     */
    Z("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),

    /**
     * 推荐完整格式（带冒号时区）：2025-04-05T14:30:25.123+08:00
     */
    FULL("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),
    ;

    private final String pattern;

    ISO8601(String pattern) {
        this.pattern = pattern;
    }
}
