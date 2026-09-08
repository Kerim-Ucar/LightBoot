package com.kerim;

import com.kerim.lightboot.annotations.application.AutoInject;
import com.kerim.lightboot.annotations.application.Service;

@Service
public class ApiController {

    @AutoInject
    private DataGetter dataGetter;

    public String getData() {
        return dataGetter.getData();
    }
}
