package com.flipkart.component.testing.shared;

import java.sql.Connection;
import java.sql.DriverManager;

class InMemoryConnection implements MysqlConnection {

    private final MysqlConnectionConfig mysqlConnectionConfig;

    InMemoryConnection(MysqlConnectionConfig mysqlConnectionConfig) {
        this.mysqlConnectionConfig = mysqlConnectionConfig;
    }

    @Override
    public Connection get() {
        String url = "jdbc:h2:mem";
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(url, "root", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnectionForDatabase() {
        String url = "jdbc:h2:mem:" + mysqlConnectionConfig.getDatabaseName() + ";DB_CLOSE_DELAY=-1";
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(url, "root", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
