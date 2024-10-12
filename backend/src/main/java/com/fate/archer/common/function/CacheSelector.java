package com.fate.archer.common.function;

@FunctionalInterface
public interface CacheSelector<T> {
    T select() throws Exception;
}
