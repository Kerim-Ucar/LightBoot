package com.kerim.lightboot.connectivity.http;

import com.kerim.lightboot.application.ApplicationComponent;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpServerComponent implements ApplicationComponent {
    public static final int DEFAULT_PORT = 8080;

    private final HttpGetRouteRegistry routeRegistry;
    private final int port;
    private HttpServer httpServer;
    private ExecutorService executorService;
    private final CountDownLatch startupLatch = new CountDownLatch(1);
    private final CountDownLatch stopLatch = new CountDownLatch(1);
    private volatile Exception startupFailure;
    private volatile boolean stopped;

    public HttpServerComponent(HttpGetRouteRegistry routeRegistry) {
        this(routeRegistry, DEFAULT_PORT);
    }

    public HttpServerComponent(HttpGetRouteRegistry routeRegistry, int port) {
        this.routeRegistry = routeRegistry;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String startup() {
        Thread serverThread = new Thread(this::startServerBlocking, "lightboot-http-server");
        serverThread.setDaemon(false);
        serverThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread(this::stop, "lightboot-http-server-shutdown"));
        return "[HttpServerComponent:" + port + "]";
    }

    public void awaitStartup() throws InterruptedException {
        startupLatch.await();
        throwIfStartupFailed();
    }

    public boolean awaitStartup(long timeout, TimeUnit unit) throws InterruptedException {
        if (!startupLatch.await(timeout, unit)) {
            return false;
        }
        throwIfStartupFailed();
        return true;
    }

    public void awaitRunning() throws InterruptedException {
        awaitStartup();
        stopLatch.await();
    }

    public void stop() {
        if (stopped) {
            return;
        }
        stopped = true;

        if (httpServer != null) {
            httpServer.stop(0);
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
        stopLatch.countDown();
    }

    private void startServerBlocking() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            executorService = Executors.newCachedThreadPool(createThreadFactory("lightboot-http-worker", false));
            httpServer.setExecutor(executorService);
            registerGetHandlers(routeRegistry.getRoutes());
            httpServer.start();
        } catch (IOException exception) {
            startupFailure = exception;
        } finally {
            startupLatch.countDown();
        }
    }

    private void throwIfStartupFailed() {
        if (startupFailure != null) {
            throw new RuntimeException("Failed to start HTTP server on port " + port, startupFailure);
        }
    }

    private static ThreadFactory createThreadFactory(String namePrefix, boolean daemon) {
        AtomicInteger threadIndex = new AtomicInteger();
        return task -> {
            Thread thread = new Thread(task, namePrefix + "-" + threadIndex.incrementAndGet());
            thread.setDaemon(daemon);
            return thread;
        };
    }

    private void registerGetHandlers(List<HttpGetRoute> routes) {
        for (HttpGetRoute route : routes) {
            httpServer.createContext(route.getPath(), exchange -> {
                if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    exchange.close();
                    return;
                }

                try {
                    Object result = route.invoke();
                    byte[] body = result == null
                            ? new byte[0]
                            : String.valueOf(result).getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
                    exchange.sendResponseHeaders(200, body.length);
                    exchange.getResponseBody().write(body);
                } catch (ReflectiveOperationException exception) {
                    byte[] body = "Internal Server Error".getBytes(StandardCharsets.UTF_8);
                    exchange.sendResponseHeaders(500, body.length);
                    exchange.getResponseBody().write(body);
                } finally {
                    exchange.close();
                }
            });
        }
    }
}
