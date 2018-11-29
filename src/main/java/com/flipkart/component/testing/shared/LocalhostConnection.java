package com.flipkart.component.testing.shared;

import java.sql.Connection;
import java.sql.DriverManager;

class LocalhostConnection implements MysqlConnection {
    private final MysqlConnectionConfig mysqlConnectionConfig;

    LocalhostConnection(MysqlConnectionConfig mysqlConnectionConfig) {
        this.mysqlConnectionConfig = mysqlConnectionConfig;
    }

    @Override
    public Connection get() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/", "root", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnectionForDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/" + mysqlConnectionConfig.getDatabaseName(), "root", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
