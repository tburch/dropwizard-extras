package com.lowtuna.dropwizard.extras.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.concurrent.ExecutorService;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ExecutorServiceConfig {

    @JsonProperty
    @NotEmpty
    private String name;

    @JsonProperty
    @Min(1)
    private int corePoolSize = 5;

    @JsonProperty
    @Min(1)
    private int maximumPoolSize = 10;

    @JsonProperty("keepAlive")
    @Valid
    @NotNull
    private Duration keepAlive = Duration.seconds(60);

    public ExecutorServiceConfig(String name) {
        this.name = name;
    }

    public ExecutorService instance(Environment environment) {
        return environment.lifecycle().executorService(name).keepAliveTime(keepAlive).minThreads(corePoolSize).maxThreads(maximumPoolSize).build();
    }

}
