package com.flipkart.component.testing.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.component.testing.model.ConnectionType;
import lombok.Data;

@Data
class ConnectionInfo {
    private final String clusterName;
    private final String host;
    private final ConnectionType connectionType;

    @JsonCreator
    public ConnectionInfo(@JsonProperty("clusterName") String clusterName, @JsonProperty("host") String host, @JsonProperty("connectionType") ConnectionType connectionType) {
        this.clusterName = clusterName;
        this.host = host;
        this.connectionType = connectionType;
    }
}
