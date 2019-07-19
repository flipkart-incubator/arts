package com.flipkart.component.testing;


import com.flipkart.component.testing.model.hbase.HbaseTestConfig;
import org.apache.hadoop.hbase.client.Table;

public interface HBaseAdminOperations {

    void startCluster();

    void stopCluster();

    void createTable(HbaseTestConfig hbaseTestConfig);

    Table getTable(HbaseTestConfig hbaseTestConfig);

    void deleteTable(HbaseTestConfig hbaseTestConfig);

    void deleteAllTables(HbaseTestConfig hbaseTestConfig);
}
