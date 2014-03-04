package com.lowtuna.dropwizard.extras.config.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Supplier;
import com.mongodb.DB;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = false, include = JsonTypeInfo.As.EXTERNAL_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MongoDbPropertiesConfig.class, name = "properties"),
        @JsonSubTypes.Type(value = MongoDbUriConfig.class, name = "uri")
})

@JsonIgnoreProperties(ignoreUnknown = true, value = "type")
public abstract class MongoDbConfig implements Supplier<DB> {
    @JsonIgnore
    public abstract DB instance();

    @Override
    public DB get() {
        return instance();
    }
}
