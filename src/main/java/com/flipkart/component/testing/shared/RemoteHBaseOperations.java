package com.flipkart.component.testing.shared;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;

import java.io.IOException;
import java.util.Arrays;

class RemoteHBaseOperations implements HBaseAdminOperations {


    @Override
    public void startCluster() {
        //this is a no-op operation as there is nothing like starting remote hbase. If it's not running already we can't help
    }

    @Override
    public void stopCluster() {
        //similar to above comment
    }

    @Override
    public void createTable(HbaseTestConfig hbaseTestConfig) {
        try {
            HBaseAdmin admin = new HBaseAdmin(getConfiguration(hbaseTestConfig));
            HTableDescriptor descriptor = new HTableDescriptor(hbaseTestConfig.getTableName());
            Arrays.stream(hbaseTestConfig.columnFamilies())
                    .map(HColumnDescriptor::new)
                    .forEach(descriptor::addFamily);
            admin.createTable(descriptor);
        } catch (IOException e) {
            throw new RuntimeException("Error while running hbase admin", e);
        }
    }

    private Configuration getConfiguration(HbaseTestConfig hbaseTestConfig) {
        Configuration configuration = HBaseConfiguration.create();
        hbaseTestConfig.getHbaseSiteConfig().forEach(configuration::set);
        return configuration;
    }


    @Override
    public HTableInterface getTable(HbaseTestConfig hbaseTestConfig) {
        try {
            HConnection connection = HConnectionManager.createConnection(this.getConfiguration(hbaseTestConfig));
            return connection.getTable(hbaseTestConfig.getTableName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTable(HbaseTestConfig hbaseTestConfig) {
        try {
            HBaseAdmin admin = new HBaseAdmin(getConfiguration(hbaseTestConfig));
            if(admin.tableExists(hbaseTestConfig.getTableName())){
                admin.disableTable(hbaseTestConfig.getTableName());
                admin.deleteTable(hbaseTestConfig.getTableName());
            }
        } catch (Exception e) {
            throw new RuntimeException("unable to delete the table " + hbaseTestConfig.getTableName(), e);
        }
    }
}
