package com.kerim.lightboot.application.context;

import com.kerim.lightboot.Test;
import com.kerim.lightboot.application.headers.Header;
import com.kerim.lightboot.exceptions.NoBeanFound;

import java.util.HashMap;
import java.util.Map;

public class ApplicationContext implements Context{

    Map<Header, Object> context = new HashMap<Header, Object>();

    @Override
    public Map<Header, Object> getContext() {
        return context;
    }

    @Override
    public <T> void register(Header header, T bean) {
        context.put(header, bean);
    }

    @Override @SuppressWarnings("unchecked")
    public <T> T get(Header header) throws NoBeanFound {
        T bean = (T) context.get(header);

        if (bean == null) {
            throw new NoBeanFound("Bean could not be found");
        }

        return bean;
    }

    public void test() {
        Test test = new Test("Test");
        Header header = new Header("test", Test.class);
        register(header, test);

        Test test2 = get(header);

        assert test == test2;
        System.out.println(test.getS());
        System.out.println(test2.getS());
    }
}
