package com.kerim;

import com.kerim.lightboot.annotations.application.Bean;
import com.kerim.lightboot.annotations.application.Configuration;

@Configuration
public class ApiKeyConfig {

    @Bean
    public DataHandler dataHandler() {
        return new DataHandler("12345");
    }

    @Bean("superData")
    public DataHandler superDataHandler() {
        return new DataHandler("67890");
    }

    @Bean
    public DataUltraHandlerWrapper dataUltraHandlerWrapper() {
        return new DataUltraHandlerWrapper("super12093", "921391");
    }
}
