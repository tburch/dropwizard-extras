package com.lowtuna.dropwizard.extras.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class ElasticSearchConfig {

    private ImmutableSettings.Builder settings = ImmutableSettings.settingsBuilder();

    @JsonProperty
    private String name;

    @JsonProperty
    private String clusterName;

    @JsonProperty
    private String dataPath;

    @JsonProperty
    private boolean http = false;

    @JsonProperty
    private boolean client = false;

    @JsonProperty
    private boolean data = true;

    @JsonProperty
    private boolean local = true;

    public ElasticSearchConfig(ImmutableSettings.Builder settings) {
        this.settings = settings;
    }

    public Node nodeInstance() {
        settings.put("http.enabled", http);
        if (StringUtils.isNotEmpty(dataPath)) {
            settings.put("path.data", dataPath);
        }
        if (StringUtils.isNotEmpty(name)) {
            settings.put("node.name", name);
        }
        NodeBuilder nodeBuilder = NodeBuilder.nodeBuilder()
                .settings(settings)
                .client(client)
                .data(data)
                .local(local);
        if (StringUtils.isNotEmpty(clusterName)) {
            nodeBuilder = nodeBuilder.clusterName(clusterName);
        }
        return nodeBuilder.node();
    }

    public Client clientInstance() {
        Node node = nodeInstance();
        return node.client();
    }
}
