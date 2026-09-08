package com.kerim.lightboot;

import com.kerim.lightboot.annotations.application.Bean;
import com.kerim.lightboot.annotations.application.Configuration;

@Configuration
public class TestBeanObj {

    @Bean
    public Test test() {
        return new Test("Test");
    }

    @Bean("test2")
    public Test test2() {
        return new Test("Test2");
    }


}
