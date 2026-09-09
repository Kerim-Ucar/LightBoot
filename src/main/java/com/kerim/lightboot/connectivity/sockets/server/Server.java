package com.kerim.lightboot.connectivity.sockets.server;

import com.kerim.lightboot.application.ApplicationComponent;
import com.kerim.lightboot.connectivity.sockets.data.Packet;

public interface Server extends ApplicationComponent {
    <T extends Packet> void send(T data);
    <T extends Packet> void receive();
}
