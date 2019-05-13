package com.flipkart.component.testing;

import com.flipkart.component.testing.model.mysql.MysqlTestConfig;

import java.sql.Connection;
import java.sql.DriverManager;

class LocalhostConnection implements MysqlConnection {
    private final MysqlTestConfig mysqlTestConfig;

    LocalhostConnection(MysqlTestConfig mysqlTestConfig) {
        this.mysqlTestConfig = mysqlTestConfig;
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
            return DriverManager.getConnection("jdbc:mysql://localhost/" + mysqlTestConfig.getDatabaseName(), "root", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
