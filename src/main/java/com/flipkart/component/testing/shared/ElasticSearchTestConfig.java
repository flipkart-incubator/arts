package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.model.ConnectionType;

public interface ElasticSearchTestConfig {

    String getClusterName();

    String getHost();

    ConnectionType getConnectionType();
}
