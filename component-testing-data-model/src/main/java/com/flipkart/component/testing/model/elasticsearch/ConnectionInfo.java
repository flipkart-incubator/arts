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
    private final String downloadURL;

    @JsonCreator
    public ConnectionInfo(@JsonProperty("clusterName") String clusterName, @JsonProperty("host") String host, @JsonProperty("connectionType") ConnectionType connectionType, @JsonProperty("downloadURL")String downloadURL) {
        this.clusterName = clusterName;
        this.host = host;
        this.connectionType = connectionType;
        this.downloadURL = downloadURL;
    }
}
