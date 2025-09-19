package io.github.wisely.core.exception.handler;

@FunctionalInterface
public interface ExceptionBuilderFunction<A, B, C, R> {

    R apply(A a, B b, C c);

}