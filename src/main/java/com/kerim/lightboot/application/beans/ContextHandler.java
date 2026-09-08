package com.kerim.lightboot.application.beans;

import com.kerim.lightboot.application.ApplicationComponent;

public interface ContextHandler extends ApplicationComponent {
    void addBeansToContext();
}
