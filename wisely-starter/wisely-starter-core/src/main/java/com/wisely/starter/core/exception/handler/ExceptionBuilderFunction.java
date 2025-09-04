package com.wisely.starter.core.exception.handler;

@FunctionalInterface
public interface ExceptionBuilderFunction<A, B, C> {

    void apply(A a, B b, C c);

}
