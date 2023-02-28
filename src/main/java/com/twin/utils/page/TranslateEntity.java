package com.twin.utils.page;

@FunctionalInterface
public interface TranslateEntity<T,M> {
    M trans(T source);
}
