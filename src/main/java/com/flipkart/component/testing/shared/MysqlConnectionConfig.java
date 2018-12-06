package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.model.mysql.MysqlConnectionType;

import java.util.ArrayList;
import java.util.List;

public interface MysqlConnectionConfig {

    String getDatabaseName();

    MysqlConnectionType getConnectionType();

    default List<String> getDdlStatements(){
        return new ArrayList<>();
    };
}
