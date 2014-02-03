package com.lowtuna.dropwizard.extras.config;

import java.net.UnknownHostException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MongoDbConfig {
    @NotEmpty
    @JsonProperty
    private String hostname = "localhost";

    @NotNull
    @Min(1)
    @Max(Short.MAX_VALUE)
    @JsonProperty
    private int port = 27017;

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @NotEmpty
    @JsonProperty
    private String database;

    public DB instance() {
        try {
            MongoClient mongoClient = new MongoClient(hostname, port);
            DB db = mongoClient.getDB(database);
            if (StringUtils.isNotBlank(username) && password != null) {
                if (!db.authenticate(username, password.toCharArray())) {
                    log.error("Couldn't authenticate with MongoDB database named {} with username {} at {}:{}", database, username, hostname, port);
                    throw new IllegalStateException("Couldn't authenticate with MongoDB (username='" + username + "') database named " + database + " at " + hostname + ":" + port);
                }
            }
            return db;
        } catch (UnknownHostException e) {
            log.error("Couldn't connected to MongoDB at {}:{}", hostname, port);
            throw new IllegalStateException("Couldn't connected to MongoDB at " + hostname + ":" + port);
        }
    }

}
