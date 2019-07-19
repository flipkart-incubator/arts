package com.flipkart.component.testing;


import com.flipkart.component.testing.model.hbase.HbaseTestConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;


import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

class RemoteHBaseOperations implements HBaseAdminOperations {
    private Configuration configuration;
    private Admin admin;
    private Connection connection;

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
            if(!getAdmin(hbaseTestConfig).tableExists(TableName.valueOf(hbaseTestConfig.getTableName()))) {
                HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(hbaseTestConfig.getTableName()));
                for (String colFamily : hbaseTestConfig.columnFamilies())
                    tableDescriptor.addFamily(new HColumnDescriptor(colFamily));
                getAdmin(hbaseTestConfig).createTable(tableDescriptor);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating hbase table "+hbaseTestConfig.getTableName(), e);
        }
    }

    private Configuration getConfiguration(HbaseTestConfig hbaseTestConfig) {
        try{
            if(configuration==null) {
                configuration = HBaseConfiguration.create();
                //dataDir/hbase
                if(!new File("dataDir/hbase/root").getAbsoluteFile().exists()) {
                    new File("dataDir/hbase/root").getAbsoluteFile().mkdirs();
                    new File("dataDir/hbase/zkr").getAbsoluteFile().mkdirs();
                }
                hbaseTestConfig.getHbaseSiteConfig().forEach(configuration::set);
            }
            return configuration;
        } catch (Exception e) {
            throw new RuntimeException("unable to create hbase configuration ", e);
        }
    }

    private Connection getConnection(HbaseTestConfig hbaseTestConfig){
        try {
            if (this.connection==null)
                this.connection = ConnectionFactory.createConnection(getConfiguration(hbaseTestConfig));
            return this.connection;
        } catch (IOException e) {
            throw new RuntimeException("Could not get Hbase connection");
        }
    }

    @Override
    public Table getTable(HbaseTestConfig hbaseTestConfig) {
        try {
            return getConnection(hbaseTestConfig).getTable(TableName.valueOf(hbaseTestConfig.getTableName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Admin getAdmin(HbaseTestConfig hbaseTestConfig){
        try {
            if(admin==null)
                admin =  getConnection(hbaseTestConfig).getAdmin();
            return admin;
        } catch (IOException e) {
            throw new RuntimeException("Unable to get admin Connection ", e);
        }

    }
    @Override
    public void deleteTable(HbaseTestConfig hbaseTestConfig) {
        try {
            if (getAdmin(hbaseTestConfig).tableExists(TableName.valueOf(hbaseTestConfig.getTableName()))) {
                admin.disableTable(TableName.valueOf(hbaseTestConfig.getTableName()));
                admin.deleteTable(TableName.valueOf(hbaseTestConfig.getTableName()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting table "+e.getMessage());
        }
    }

    @Override
    public void deleteAllTables(HbaseTestConfig hbaseTestConfig){
        try{
           getAdmin(hbaseTestConfig).disableTables(Pattern.compile("regression_.*"));
           getAdmin(hbaseTestConfig).deleteTables(Pattern.compile("regression_.*"));
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
