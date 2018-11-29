package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.model.ConnectionType;

import java.util.Map;

public interface HbaseTestConfig {

    String getTableName();

    String[] columnFamilies();

    ConnectionType getConnectionType();

    Map<String,String> getHbaseSiteConfig();

    boolean shouldCreateTable();

}
