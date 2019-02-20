package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.solr.SolrData;

import java.util.List;

public interface SolrTestConfig {
    String getSolrCoreName();
    ConnectionType getConnectionType();
    String getConfigFilePath();
}
