package com.flipkart.component.testing;

import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlTestConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

class LocalhostConnection implements MysqlConnection {
    private final MysqlTestConfig mysqlTestConfig;
    private Connection mySqlConnection;
    private Connection dbConnection;

    LocalhostConnection(MysqlTestConfig mysqlTestConfig) {
        this.mysqlTestConfig = mysqlTestConfig;
    }

    @Override
    public Connection get() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if (mySqlConnection==null)
                mySqlConnection = DriverManager.getConnection("jdbc:mysql://localhost/", "root", "");
            return mySqlConnection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnectionForDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            if(dbConnection==null)
                dbConnection =DriverManager.getConnection("jdbc:mysql://localhost/" + mysqlTestConfig.getDatabaseName(), "root", "");
            return dbConnection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createDatabase() {
        if(mysqlTestConfig.getConnectionType() == MysqlConnectionType.IN_MEMORY) return;
        try {
            this.executeDatabaseStatement("CREATE DATABASE IF NOT EXISTS " + mysqlTestConfig.getDatabaseName(), get());
        } catch(Exception e){
            throw new RuntimeException("Could not connect to MySql", e);
        } finally {
            try {
                get().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void runDDLStatements() {
        Optional.ofNullable(mysqlTestConfig.getDdlStatements()).orElse(new ArrayList<>()).forEach(sql ->
                this.executeDatabaseStatement(sql, getConnectionForDatabase()));
    }


    public void dropDatabase(String database){
        String dropDatabase = "DROP DATABASE IF EXISTS " + database;
        this.executeDatabaseStatement(dropDatabase, MysqlFactory.getMysqlConnection(mysqlTestConfig).get());
    }


    private void executeDatabaseStatement(String sql, Connection connection) {
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropAllTables(){
        try {
            ResultSet tables = getConnectionForDatabase().getMetaData().getTables(null, null, "%", null);
            while (tables.next()){
                executeDatabaseStatement("DROP TABLE IF EXISTS "+tables.getString(3),getConnectionForDatabase());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not drop all the tables : "+e);
        }
    }
}
