package io.github.wisely.starter.core.data.eum;

import lombok.Getter;

@Getter
public enum YearMonth {

    DASH("yyyy-MM"),
    SLASH("yyyy/MM"),
    COMPACT("yyyyMM"),
    ;

    private final String pattern;

    YearMonth(String pattern) {
        this.pattern = pattern;
    }
}
