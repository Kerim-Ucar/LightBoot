package com.kerim.lightboot.application.context;

import com.kerim.lightboot.application.headers.Header;
import com.kerim.lightboot.exceptions.NoBeanFound;
import com.kerim.lightboot.exceptions.NoHeaderFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext implements Context {

    Map<Header, Object> context = new HashMap<Header, Object>();
    ArrayList<Header> headers = new ArrayList<Header>();

    @Override
    public Map<Header, Object> getContext() {
        return context;
    }

    @Override
    public <T> void register(Header header, T bean) {
        context.put(header, bean);
        headers.add(header);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Header header) throws NoBeanFound {
        T bean = (T) context.get(header);

        if (bean == null) {
            throw new NoBeanFound("Bean could not be found");
        }

        return bean;
    }

    public Header lookUpHeader(String headerName) {
        for (Header header : headers) {
            if (header.name().equals(headerName)) {
                return header;
            }
        }
        throw new NoHeaderFound("ApplicationContext.lookUpHeader() failed. Header name: " + headerName + " not found.");
    }

    public Header lookUpHeader(Class<?> clazz) {
        for (Header header : headers) {
            if (header.clazz().equals(clazz)) {
                return header;
            }
        }
        throw new NoHeaderFound("ApplicationContext.lookUpHeader() failed. Header class: " + clazz + " not found.");
    }

    public boolean isRegistered(Class<?> clazz) {
        for (Header header : headers) {
            if (header.clazz().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void printContext() {
        for (Map.Entry<Header, Object> entry : context.entrySet()) {
            Header header = entry.getKey();
            Object bean = entry.getValue();
            System.out.println(header.name() + " (" + header.clazz().getSimpleName() + ") -> " + bean);
        }
    }

    public void test() {

    }
}
