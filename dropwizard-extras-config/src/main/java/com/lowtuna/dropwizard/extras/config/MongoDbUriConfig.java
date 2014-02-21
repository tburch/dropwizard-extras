package com.lowtuna.dropwizard.extras.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import java.net.UnknownHostException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MongoDbUriConfig extends MongoDbConfig {

    @NotEmpty
    @JsonProperty
    private String uri;

    @Override
    @JsonIgnore
    public DB instance() {
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        try {
            MongoClient mongoClient = new MongoClient(mongoClientURI);
            DB db = mongoClient.getDB(mongoClientURI.getDatabase());
            return db;
        } catch (UnknownHostException e) {
            MongoDbUriConfig.log.error("Couldn't connect to MongoDB at uri={}", uri);
            throw new IllegalStateException("Couldn't connect to MongoDB at " + StringUtils.join(mongoClientURI.getHosts(), ", "));
        }
    }
}
