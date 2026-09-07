package com.kerim.lightboot;

import com.kerim.lightboot.annotations.Bean;

public class TestBeanObj {

    @Bean
    public Test test() {
        return new Test("Test");
    }


}
