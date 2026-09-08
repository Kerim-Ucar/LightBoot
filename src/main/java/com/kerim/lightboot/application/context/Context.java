package com.kerim.lightboot.application.context;

import com.kerim.lightboot.application.headers.Header;
import com.kerim.lightboot.exceptions.NoBeanFound;

import java.util.Map;

public interface Context {
    <T> void register(Header header, T bean);
    <T> T get(Header header) throws NoBeanFound;
    Map<Header, Object> getContext();
}
