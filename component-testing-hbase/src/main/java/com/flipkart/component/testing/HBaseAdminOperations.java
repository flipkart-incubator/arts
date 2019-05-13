package com.flipkart.component.testing;


import com.flipkart.component.testing.model.hbase.HbaseTestConfig;
import org.apache.hadoop.hbase.client.HTableInterface;

public interface HBaseAdminOperations {

    void startCluster();

    void stopCluster();

    void createTable(HbaseTestConfig hbaseTestConfig);

    HTableInterface getTable(HbaseTestConfig hbaseTestConfig);

    void deleteTable(HbaseTestConfig hbaseTestConfig);
}
