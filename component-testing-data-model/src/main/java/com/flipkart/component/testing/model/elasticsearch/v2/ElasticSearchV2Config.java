package com.flipkart.component.testing.model.elasticsearch.v2;

import com.flipkart.component.testing.model.elasticsearch.ElasticSearchTestConfig;

import java.util.Optional;

public abstract class ElasticSearchV2Config implements ElasticSearchTestConfig {

    protected final ConnectionInfo connectionInfo;

    ElasticSearchV2Config(ConnectionInfo connectionInfo) {
        this.connectionInfo = connectionInfo;
    }

    public String getUsername() {
        return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getUsername).orElse("");
    }

    public String getPassword() {
        return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getPassword).orElse("");
    }

    public Integer getPort() {
        return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getPort).orElse(9200);
    }

    @Override
    public String getClusterDownloadURL() {
        throw new UnsupportedOperationException("Cluster download operation not supported for ES version 7.3.1");
    }
}
