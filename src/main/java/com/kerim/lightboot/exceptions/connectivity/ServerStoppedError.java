package com.kerim.lightboot.exceptions.connectivity;

public class ServerStoppedError extends RuntimeException {
    public ServerStoppedError(String message) {
        super(message);
    }
}
