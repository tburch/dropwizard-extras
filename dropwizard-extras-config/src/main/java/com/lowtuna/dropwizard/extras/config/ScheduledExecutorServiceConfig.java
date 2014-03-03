package com.lowtuna.dropwizard.extras.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.setup.Environment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.util.concurrent.ScheduledExecutorService;

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

    @JsonIgnore
    public ScheduledExecutorService instance(Environment environment) {
        return environment.lifecycle().scheduledExecutorService(name).threads(poolSize).build();
    }

}
