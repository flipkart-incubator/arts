package com.flipkart.component.testing;


import org.apache.hadoop.hbase.client.HTable;

public interface HBaseAdminOperations {

    void startCluster();

    void stopCluster();

    void createTable();

    HTable getTable();

    void deleteTable();

    void deleteAllTables();
}
