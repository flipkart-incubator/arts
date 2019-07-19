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
                mysqlConnection = DriverManager.getConnection(url, "root", "");
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
                dbConnection= DriverManager.getConnection(url, "root", "");
            return dbConnection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void createDatabase() {
        if(mysqlTestConfig.getConnectionType() == MysqlConnectionType.IN_MEMORY) return;
        try {
            String createDatabase = "CREATE DATABASE IF NOT EXISTS " + mysqlTestConfig.getDatabaseName();
            this.executeDatabaseStatement(createDatabase, get());

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
        String dropDatabase = "DROP DATABASE IF EXISTS " +database;
        this.executeDatabaseStatement(dropDatabase, get());
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
