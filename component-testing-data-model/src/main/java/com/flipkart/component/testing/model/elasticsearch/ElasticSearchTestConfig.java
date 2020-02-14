package com.flipkart.component.testing.model.elasticsearch;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.TestConfig;

public interface ElasticSearchTestConfig extends TestConfig{

    String getClusterName();

    String getHost();

    ConnectionType getConnectionType();

    String getClusterDownloadURL();
}
