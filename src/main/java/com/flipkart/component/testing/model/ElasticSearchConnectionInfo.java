package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ElasticSearchConnectionInfo {
    private final String clusterName;
    private final String host;
    private final ConnectionType connectionType;

    @JsonCreator
    public ElasticSearchConnectionInfo(@JsonProperty("clusterName") String clusterName, @JsonProperty("host") String host, @JsonProperty("connectionType") ConnectionType connectionType) {
        this.clusterName = clusterName;
        this.host = host;
        this.connectionType = connectionType;
    }
}
