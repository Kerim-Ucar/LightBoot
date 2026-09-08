package com.kerim.lightboot.application.headers;

public class SimpleHeaderFactory implements HeaderFactory {
    private final String LOGGER_RETURN_STRING = "[SimpleHeaderFactory]";

    @Override
    public Header createHeader(String name, Class<?> clazz) {
        return new Header(name, clazz);
    }

    @Override
    public Header createHeader(Class<?> clazz) {
        return new Header(clazz.getSimpleName(), clazz);
    }

    @Override
    public String startup() {
        return LOGGER_RETURN_STRING;
    }
}
