package com.lowtuna.dropwizard.extras.heroku;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import io.dropwizard.util.Duration;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AntiIdler implements Runnable {
    private final boolean https;
    private final String hostname;
    private final int port;
    private final String context;
    private final Duration interval;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Client jerseyClient;

    public AntiIdler(boolean https, String hostname, int port, String context, Duration interval, ScheduledExecutorService scheduledExecutorService, Client jerseyClient) {
        this.https = https;
        this.hostname = hostname;
        this.port = port;
        this.context = context;
        this.interval = interval;
        this.jerseyClient = jerseyClient;
        this.scheduledExecutorService = scheduledExecutorService;

        scheduledExecutorService.scheduleAtFixedRate(this, this.interval.getQuantity(), this.interval.getQuantity(), this.interval.getUnit());
    }

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder(https ? "https" : "http");
        sb.append("://");
        sb.append(hostname);
        sb.append(":");
        sb.append(port);
        sb.append(context);

        WebResource webResource = jerseyClient.resource(sb.toString());
        try {
            ClientResponse response = webResource.get(ClientResponse.class);
            log.debug("Received {} response from {}", response.getStatus(), sb.toString());
        } catch (Exception e) {
            log.warn("Exception when trying to make request to {}. Retrying in 10 seconds", sb.toString(), e);
            scheduledExecutorService.schedule(this, 10, TimeUnit.SECONDS);
        }
    }

}
