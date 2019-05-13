package com.flipkart.component.testing.model.solr;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.TestConfig;

public interface SolrTestConfig extends TestConfig {
    String getSolrCoreName();
    ConnectionType getConnectionType();
    String getConfigFilePath();
}
