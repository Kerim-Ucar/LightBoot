package com.kerim.lightboot.connectivity.sockets.server;

import java.io.IOException;

public interface ServerComponent {
    Server getServer();
    void start() throws IOException;
    void stop();
}
