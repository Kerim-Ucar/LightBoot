package com.kerim.lightboot.application.headers;

public interface HeaderFactory {
    Header createHeader(String name, Class<?> clazz);
    Header createHeader(Class<?> clazz);
}
