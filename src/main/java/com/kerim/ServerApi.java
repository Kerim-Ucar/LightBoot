package com.kerim;

import com.kerim.lightboot.annotations.application.AutoInject;
import com.kerim.lightboot.annotations.application.Service;
import com.kerim.lightboot.connectivity.sockets.server.LocalServer;
import com.kerim.lightboot.connectivity.sockets.server.Server;

import java.io.IOException;

@Service
public class ServerApi {

    @AutoInject
    private Server server;

    public ServerApi(Server server) throws IOException {
        this.server = server;
    }

    public void start() throws IOException {
        server.start();
    }

    public void send(String data) throws IOException {
        server.send(data);
    }
}
