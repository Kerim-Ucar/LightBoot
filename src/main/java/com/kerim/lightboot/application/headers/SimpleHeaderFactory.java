package com.kerim.lightboot.application.headers;

public class SimpleHeaderFactory implements HeaderFactory {

    @Override
    public Header createHeader(String name, Class<?> clazz) {
        return new Header(name, clazz);
    }

    @Override
    public Header createHeader(Class<?> clazz) {
        return new Header(clazz.getSimpleName(), clazz);
    }
}
