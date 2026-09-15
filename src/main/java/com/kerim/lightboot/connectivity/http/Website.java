package com.kerim.lightboot.connectivity.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class Website {
    private final String url;
    private final String name;
    private final HttpClient client;
    private Map<String, HttpRequest> requestMap;

    public Website(String url, String name, HttpClient client) {
        this.url = url;
        this.name = name;
        this.client = client;
        this.requestMap = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public HttpClient getClient() {
        return client;
    }

    public Map<String, HttpRequest> getRequestMap() {
        return requestMap;
    }

    public <T> HttpResponse<T> sendBuiltinRequest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return SendRequest(request, HttpType.GET);
    }


    @SuppressWarnings("unchecked")
    private <T> HttpResponse<T> SendRequest(HttpRequest request, HttpType type) throws IOException, InterruptedException {
        return (HttpResponse<T>) client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @SuppressWarnings("unchecked")
    private <T> HttpResponse<T> sendAsyncRequest(HttpRequest request, HttpType type) {
        return (HttpResponse<T>) client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public <T> HttpResponse<T> SendRequest(HttpRequest request, String alias, HttpType type, PollingMethod pollingMethod) throws IOException, InterruptedException {
        requestMap.put(alias, request);
        if(pollingMethod == PollingMethod.ASYNC) {
            return sendAsyncRequest(request, type);
        } else {
            return SendRequest(request, type);
        }
    }


}
