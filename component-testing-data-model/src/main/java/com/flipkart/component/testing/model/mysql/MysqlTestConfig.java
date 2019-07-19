package com.flipkart.component.testing.model.mysql;

import com.flipkart.component.testing.model.TestConfig;

import java.util.ArrayList;
import java.util.List;

public interface MysqlTestConfig extends TestConfig {

    String getDatabaseName();

    ConnectionInfo getConnectionInfo();

    default List<String> getDdlStatements(){
        return new ArrayList<>();
    };
}
