package io.github.wisely.starter.core.data.eum;

import lombok.Getter;

@Getter
public enum TimeOnly {

    COLON("HH:mm:ss"),
    COMPACT("HHmmss"),
    MINUTE("HH:mm"),
    ;

    private final String pattern;

    TimeOnly(String pattern) {
        this.pattern = pattern;
    }
}
