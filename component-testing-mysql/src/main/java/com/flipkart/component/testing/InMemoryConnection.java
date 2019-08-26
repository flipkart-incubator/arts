package com.flipkart.component.testing;


import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlTestConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

class InMemoryConnection implements MysqlConnection {

    private final MysqlTestConfig mysqlTestConfig;
    private Connection mysqlConnection;
    private Connection dbConnection;

    InMemoryConnection(MysqlTestConfig mysqlTestConfig) {
        this.mysqlTestConfig = mysqlTestConfig;
    }

    @Override
    public Connection get() {
        String url = "jdbc:h2:mem";
        try {
            Class.forName("org.h2.Driver");
            if(mysqlConnection==null)
                mysqlConnection = DriverManager.getConnection(url, mysqlTestConfig.getConnectionInfo().getUser(), mysqlTestConfig.getConnectionInfo().getPassword());
            return mysqlConnection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnectionForDatabase() {
        String url = "jdbc:h2:mem:" + mysqlTestConfig.getDatabaseName() + ";DB_CLOSE_DELAY=-1";
        try {
            Class.forName("org.h2.Driver");
            if(dbConnection==null)
                dbConnection= DriverManager.getConnection(url, mysqlTestConfig.getConnectionInfo().getUser(), mysqlTestConfig.getConnectionInfo().getPassword());
            return dbConnection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void createDatabase() {
        //create DB is not required in in-memory
    }

    @Override
    public void runDDLStatements() {
        Optional.ofNullable(mysqlTestConfig.getDdlStatements()).orElse(new ArrayList<>()).forEach(sql ->
                this.executeDatabaseStatement(sql, getConnectionForDatabase()));
    }

    @Override
    public void dropDatabase(String database) {
        //drop DB is not required in in-memory
    }


    private void executeDatabaseStatement(String sql, Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
