package com.lowtuna.dropwizard.extras.config;

import java.util.concurrent.ScheduledExecutorService;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.setup.Environment;
import lombok.*;
import lombok.experimental.Builder;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ScheduledExecutorServiceConfig {

    @JsonProperty
    @NotEmpty
    private String name;

    @JsonProperty
    @Min(1)
    private int poolSize = 10;

    public ScheduledExecutorServiceConfig(String name) {
        this.name = name;
    }

    public ScheduledExecutorService instance(Environment environment) {
        return environment.lifecycle().scheduledExecutorService(name).threads(poolSize).build();
    }

}
