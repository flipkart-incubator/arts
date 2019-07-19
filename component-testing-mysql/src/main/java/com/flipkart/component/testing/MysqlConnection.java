package com.flipkart.component.testing;

import java.sql.Connection;
import java.util.List;

public interface MysqlConnection {

    Connection get();

    Connection getConnectionForDatabase();

    void createDatabase();

    void runDDLStatements();

    void dropDatabase(String database);

    void dropAllTables();

}
