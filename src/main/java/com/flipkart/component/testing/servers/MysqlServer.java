package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.model.MysqlConnectionType;
import com.flipkart.component.testing.shared.MysqlConnectionConfig;
import com.flipkart.component.testing.shared.ObjectFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

class MysqlServer extends DependencyInitializer{

    private final MysqlConnectionConfig connectionConfig;

    MysqlServer(MysqlConnectionConfig connectionConfig){
        this.connectionConfig = connectionConfig;
    }

    @Override
    public void initialize()  {
        createDatabase();
        runDDLStatements();
    }

    @Override
    public void shutDown() {

    }

    /**
     * create database and tables with the provided information from the indirect input
     */
    private void runDDLStatements() {
        Connection connectionForDatabase = ObjectFactory.getMysqlConnection(connectionConfig).getConnectionForDatabase();
        Optional.ofNullable(connectionConfig.getDdlStatements()).orElse(new ArrayList<>()).forEach(sql -> this.executeDatabaseStatement(sql, connectionForDatabase));
    }

    private void createDatabase() {
        //for in memory db database creation is not required.
        if(connectionConfig.getConnectionType() == MysqlConnectionType.IN_MEMORY) return;
        Connection connection = null;
        try {
            connection = ObjectFactory.getMysqlConnection(connectionConfig).get();
            String createDatabase = "CREATE DATABASE IF NOT EXISTS " + connectionConfig.getDatabaseName();
            String dropDatabase = "DROP DATABASE IF EXISTS " + connectionConfig.getDatabaseName();
            this.executeDatabaseStatement(dropDatabase, connection);
            this.executeDatabaseStatement(createDatabase, connection);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeDatabaseStatement(String sql, Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException ignored) {
            }
        }
    }

}
