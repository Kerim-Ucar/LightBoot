package com.kerim.lightboot.application.context;

import com.kerim.lightboot.application.headers.Header;

import java.util.Map;

public interface Context {
    <T> void register(Header header, T bean);
    <T> T get(Header header);
    Map<Header, Object> getContext();
}
