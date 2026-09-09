package com.kerim.lightboot.connectivity.sockets.data;

@FunctionalInterface
public interface Packet {
    <T> T unpack();
}
