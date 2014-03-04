package com.lowtuna.dropwizard.extras.heroku;

import com.lowtuna.dropwizard.extras.config.executors.ScheduledExecutorServiceConfig;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AntiIdlerConfig {

    @NotNull
    private Boolean https = Boolean.FALSE;

    @NotEmpty
    private String hostname = "localhost";

    @NotNull
    private Integer port = 80;

    @NotEmpty
    private String context = "/";

    @NotNull
    private Duration interval = Duration.minutes(59);

    @NotNull
    private ScheduledExecutorServiceConfig scheduledExecutorService = new ScheduledExecutorServiceConfig("AntiIdler-%d");

    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

}
