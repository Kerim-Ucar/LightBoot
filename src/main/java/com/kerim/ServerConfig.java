package com.kerim;


import com.kerim.lightboot.annotations.application.Bean;
import com.kerim.lightboot.annotations.application.Configuration;
import com.kerim.lightboot.connectivity.sockets.server.LocalServer;
import com.kerim.lightboot.connectivity.sockets.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayDeque;

@Configuration
public class ServerConfig {

    @Bean
    public Server localServer() throws IOException {
        Server server = new LocalServer(
                8080,
                "127.0.0.1",
                "LightBootLocalServer",
                new ServerSocket(8080),
                new ArrayDeque<>(),
                new ArrayDeque<>()
        );

        return server;
    }

    @Bean
    public A a() {
        return new A();
    }

    @Bean
    public B b() {
        return new B();
    }


}
