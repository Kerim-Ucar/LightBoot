package com.kerim.lightboot.application.headers;

import com.kerim.lightboot.application.ApplicationComponent;

public interface HeaderFactory extends ApplicationComponent {
    Header createHeader(String name, Class<?> clazz);

    Header createHeader(Class<?> clazz);
}
