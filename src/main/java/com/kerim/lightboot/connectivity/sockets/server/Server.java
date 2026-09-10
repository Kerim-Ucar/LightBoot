package com.kerim.lightboot.connectivity.sockets.server;

import com.kerim.lightboot.application.ApplicationComponent;
import com.kerim.lightboot.connectivity.sockets.data.Packet;

import java.io.IOException;

public interface Server extends ApplicationComponent, ServerComponent{
            void send(Packet data) throws IOException;
            void send(String data) throws IOException;
            Packet receive() throws IOException, ClassNotFoundException;
}
