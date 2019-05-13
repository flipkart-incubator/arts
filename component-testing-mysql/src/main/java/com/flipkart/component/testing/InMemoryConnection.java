package com.flipkart.component.testing;


import com.flipkart.component.testing.model.mysql.MysqlTestConfig;

import java.sql.Connection;
import java.sql.DriverManager;

class InMemoryConnection implements MysqlConnection {

    private final MysqlTestConfig mysqlTestConfig;

    InMemoryConnection(MysqlTestConfig mysqlTestConfig) {
        this.mysqlTestConfig = mysqlTestConfig;
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
        String url = "jdbc:h2:mem:" + mysqlTestConfig.getDatabaseName() + ";DB_CLOSE_DELAY=-1";
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(url, "root", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
