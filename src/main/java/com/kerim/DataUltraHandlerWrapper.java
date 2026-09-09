package com.kerim;

public class DataUltraHandlerWrapper extends DataHandler{
    private String superUltraHander;

    public DataUltraHandlerWrapper(String superUltraHander, String apiKey) {
        super(apiKey);
        this.superUltraHander = superUltraHander;
    }

    public String getSuperUltraHander() {
        return superUltraHander;
    }
}
