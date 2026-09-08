package com.kerim;

import com.kerim.lightboot.annotations.application.AutoInject;
import com.kerim.lightboot.annotations.application.Service;

@Service
public class DataGetter {

    @AutoInject("superData")
    private DataHandler dataHandler;

    public DataGetter(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    public String getData() {
        return dataHandler.getData();
    }
}
