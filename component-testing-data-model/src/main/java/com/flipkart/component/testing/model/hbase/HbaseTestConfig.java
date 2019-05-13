package com.flipkart.component.testing.model.hbase;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.TestConfig;

import java.util.Map;

public interface HbaseTestConfig extends TestConfig{

    String getTableName();

    String[] columnFamilies();

    ConnectionType getConnectionType();

    Map<String,String> getHbaseSiteConfig();

    boolean shouldCreateTable();

}
