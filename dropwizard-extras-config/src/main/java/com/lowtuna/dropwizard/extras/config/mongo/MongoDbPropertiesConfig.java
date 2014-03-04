package com.lowtuna.dropwizard.extras.config.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.UnknownHostException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class MongoDbPropertiesConfig extends MongoDbConfig {

    @NotEmpty
    @JsonProperty
    private String hostname = "localhost";

    @NotNull
    @Min(1)
    @JsonProperty
    private int port = 27017;

    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    @NotEmpty
    @JsonProperty
    private String database;

    @Override
    @JsonIgnore
    public DB instance() {
        try {
            MongoClient mongoClient = new MongoClient(hostname, port);
            DB db = mongoClient.getDB(database);
            if (StringUtils.isNotBlank(username) && password != null) {
                if (!db.authenticate(username, password.toCharArray())) {
                    MongoDbPropertiesConfig.log.error("Couldn't authenticate with MongoDB database named {} with username {} at {}:{}", database, username, hostname, port);
                    throw new IllegalStateException("Couldn't authenticate with MongoDB (username='" + username + "') database named " + database + " at " + hostname + ":" + port);
                }
            }
            return db;
        } catch (UnknownHostException e) {
            MongoDbPropertiesConfig.log.error("Couldn't connect to MongoDB at {}:{}", hostname, port);
            throw new IllegalStateException("Couldn't connect to MongoDB at " + hostname + ":" + port);
        }
    }

}
