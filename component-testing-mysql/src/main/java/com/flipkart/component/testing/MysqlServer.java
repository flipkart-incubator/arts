package com.flipkart.component.testing;

import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlIndirectInput;
import com.flipkart.component.testing.model.mysql.MysqlObservation;
import com.flipkart.component.testing.model.mysql.MysqlTestConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

class MysqlServer implements DependencyInitializer<MysqlIndirectInput,MysqlObservation,MysqlTestConfig>{

    private MysqlTestConfig testConfig;

    @Override
    public void initialize(MysqlTestConfig testConfig) throws Exception {
        this.testConfig = testConfig;
        createDatabase();
        runDDLStatements();
    }

    @Override
    public void shutDown() {

    }

    @Override
    public void clean() {
        dropDatabase();
        try {
            initialize(this.testConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<MysqlIndirectInput> getIndirectInputClass() {
        return MysqlIndirectInput.class;
    }

    @Override
    public Class<MysqlObservation> getObservationClass() {
        return MysqlObservation.class;
    }

    /**
     * create database and tables with the provided information from the indirect input
     */
    private void runDDLStatements() {
        Connection connectionForDatabase = MysqlFactory.getMysqlConnection(testConfig).getConnectionForDatabase();
        Optional.ofNullable(testConfig.getDdlStatements()).orElse(new ArrayList<>()).forEach(sql -> this.executeDatabaseStatement(sql, connectionForDatabase));
    }

    private void createDatabase() {
        //for in memory db database creation is not required.
        if(testConfig.getConnectionType() == MysqlConnectionType.IN_MEMORY) return;
        Connection connection = null;
        try {
            connection = MysqlFactory.getMysqlConnection(testConfig).get();
            dropDatabase();
            String createDatabase = "CREATE DATABASE IF NOT EXISTS " + testConfig.getDatabaseName();
            this.executeDatabaseStatement(createDatabase, connection);

        } catch(Exception e){
            throw new RuntimeException("Could not connect to MySql", e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void dropDatabase(){
        String dropDatabase = "DROP DATABASE IF EXISTS " + testConfig.getDatabaseName();
        this.executeDatabaseStatement(dropDatabase, MysqlFactory.getMysqlConnection(testConfig).get());
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
