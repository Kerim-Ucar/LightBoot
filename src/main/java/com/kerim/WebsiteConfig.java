package com.kerim;

import com.kerim.lightboot.annotations.application.Bean;
import com.kerim.lightboot.annotations.application.Configuration;
import com.kerim.lightboot.connectivity.http.Website;

@Configuration
public class WebsiteConfig {
    @Bean
    public Website website() {
        return new Website(null, null, null);
    }
}
