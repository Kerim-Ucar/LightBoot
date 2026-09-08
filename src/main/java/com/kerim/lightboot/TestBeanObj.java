package com.kerim.lightboot;

import com.kerim.lightboot.annotations.Bean;
import com.kerim.lightboot.annotations.Configuration;

@Configuration
public class TestBeanObj {

    @Bean
    public Test test() {
        return new Test("Test");
    }

    @Bean()
    public Test test2() {
        return new Test("Test2");
    }


}
