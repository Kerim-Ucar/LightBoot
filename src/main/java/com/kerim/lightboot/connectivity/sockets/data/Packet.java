package com.kerim.lightboot.connectivity.sockets.data;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

public interface Packet extends Serializable {
    @Serial
    long serialVersionUID = 1L;

    <T> T get();
    Instant getTimestamp();
}
