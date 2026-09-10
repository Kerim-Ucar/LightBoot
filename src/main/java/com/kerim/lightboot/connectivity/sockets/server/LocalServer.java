package com.kerim.lightboot.connectivity.sockets.server;

import com.kerim.lightboot.annotations.application.Service;
import com.kerim.lightboot.connectivity.sockets.data.Packet;
import com.kerim.lightboot.connectivity.sockets.data.Status;
import com.kerim.lightboot.exceptions.connectivity.ServerNotStarted;
import com.kerim.lightboot.exceptions.connectivity.ServerStoppedError;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;


public class LocalServer implements Server {
    private final String LOGGER_STRING_RETURN;
    private Status status;
    private final int port;
    private final String ip;
    private final String serverName;
    private final ServerSocket serverSocket;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Queue<Packet> sentPackets;
    private Queue<Packet> recievedPackets;

    public LocalServer(
            int port,
            String ip,
            String serverName,
            ServerSocket serverSocket,
            Queue<Packet> sentPackets,
            Queue<Packet> receivedPackets
    ) throws IOException {
        this.port = port;
        this.ip = ip;
        this.serverName = serverName;
        this.serverSocket = serverSocket;
        this.socket = null;
        this.inputStream = null;
        this.outputStream = null;
        this.status = Status.CREATED;
        this.LOGGER_STRING_RETURN = "[LocalServer:"+port+"]";
        this.sentPackets = sentPackets;
        this.recievedPackets = receivedPackets;

    }

    private void checkStatus() {
        if (status == Status.STOPPED) {
            throw new ServerStoppedError("Server [" + serverName + "] was called while it was in status: " + status + ".");
        } else if (status == Status.CREATED) {
            throw new ServerNotStarted("Server [" + serverName + "] was called while it was in status: " + status + ".");
        }
    }

    @Override
    public void send(Packet data) throws IOException {
        checkStatus();
        sentPackets.add(data);
        outputStream.writeObject(data);
    }

    public void send(String data) throws IOException {
        checkStatus();
        outputStream.writeObject(data);
    }

    @Override
    public Packet receive() throws IOException, ClassNotFoundException {
        checkStatus();
        Packet packet = (Packet) inputStream.readObject();
        recievedPackets.add(packet);
        return packet;
    }

    private void connect() throws IOException {
        status = Status.WAITING;
        socket = serverSocket.accept();
        socket.setKeepAlive(true);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());

        String testConnectionMessage = "["+serverName+": " + ip + ":" + port + "]";
        outputStream.writeObject(testConnectionMessage);
        outputStream.flush();
    }

    @Override
    public String startup() {
        return LOGGER_STRING_RETURN;
    }

    @Override
    public Server getServer() {
        return this;
    }

    @Override
    public void start() throws IOException {
        connect();
        this.status = Status.RUNNING;
    }

    @Override
    public void stop() {
        this.status = Status.STOPPED;
    }
}
