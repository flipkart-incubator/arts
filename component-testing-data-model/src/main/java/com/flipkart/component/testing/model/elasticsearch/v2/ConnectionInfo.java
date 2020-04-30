package com.flipkart.component.testing.model.elasticsearch.v2;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.component.testing.model.ConnectionType;
import lombok.Data;

@Data
class ConnectionInfo {
    private final String clusterName;
    private final String host;
    private final ConnectionType connectionType;
    private final String username;
    private final String password;
    private final Integer port;

    @JsonCreator
    public ConnectionInfo(@JsonProperty("clusterName") String clusterName, @JsonProperty("host") String host,
                          @JsonProperty("connectionType") ConnectionType connectionType,
                          @JsonProperty("username") String username,
                          @JsonProperty("password") String password, @JsonProperty("port") Integer port) {
        this.clusterName = clusterName;
        this.host = host;
        this.connectionType = connectionType;
        this.username = username;
        this.password = password;
        this.port = port;
    }
}
