package com.wisely.starter.core.data.eum;

import lombok.Getter;

@Getter
public enum DateOnly {

    DASH("yyyy-MM-dd"),
    SLASH("yyyy/MM/dd"),
    COMPACT("yyyyMMdd"),
    ;

    private final String pattern;

    DateOnly(String pattern) {
        this.pattern = pattern;
    }
}
