package com.lowtuna.dropwizard.extras.heroku;

import com.sun.jersey.api.client.Client;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.concurrent.ScheduledExecutorService;

public abstract class AntiIdlerBundle<T extends Configuration> implements ConfiguredBundle<T> {
    @Override
    public void run(T configuration, Environment environment) throws Exception {
        AntiIdlerConfig config = getConfig(configuration);

        ScheduledExecutorService scheduledExecutorService = config.getScheduledExecutorService().instance(environment);
        Client jerseyClient = new JerseyClientBuilder(environment).using(config.getJerseyClient()).build("AntiIdlerJerseyClient");

        new AntiIdler(config.getHttps(), config.getHostname(), config.getPort(), config.getContext(), config.getInterval(), scheduledExecutorService, jerseyClient);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        //nothing to do
    }

    public abstract AntiIdlerConfig getConfig(T configuration);

}
