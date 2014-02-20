package com.lowtuna.dropwizard.extras.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Collections;
import java.util.Set;

@Getter
@Setter
public class GoogleAnalyticsConfig {
    @JsonProperty
    @NotEmpty
    private String webPropertyID;

    @JsonProperty
    private Set<CustomTrackingCode> customTrackingCodes = Collections.emptySet();

    @Getter
    @Setter
    public static class CustomTrackingCode {
        @JsonProperty
        @NotEmpty
        private String key;

        @JsonProperty
        private String value = StringUtils.EMPTY;
    }
}

