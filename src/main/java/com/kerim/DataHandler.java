package com.kerim;

import com.kerim.lightboot.annotations.application.Service;

@Service
public class DataHandler {

    private String apiKey;

    public DataHandler(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getData() {
        return apiKey;
    }
}
