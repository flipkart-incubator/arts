package com.flipkart.component.testing.model.hbase;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.TestConfig;

import java.util.Map;
import java.util.Set;

public interface HbaseTestConfig extends TestConfig{

    String getTableName();

    Set<String> columnFamilies();

    ConnectionType getConnectionType();

    Map<String,String> getHbaseSiteConfig();

}
