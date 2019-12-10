package com.flipkart.component.testing;


import com.flipkart.component.testing.model.hbase.HbaseTestConfig;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

class RemoteHBaseOperations implements HBaseAdminOperations {

    private HbaseTestConfig hbaseTestConfig;
    private HBaseAdmin hBaseAdmin;
    private HBaseConfiguration hBaseConfiguration;

    RemoteHBaseOperations(HbaseTestConfig hbaseTestConfig) {
        this.hbaseTestConfig= hbaseTestConfig;
        try {
            hBaseAdmin = new HBaseAdmin(getConfiguration());
        } catch (MasterNotRunningException e) {
            throw new RuntimeException("Master not running in the given host : "+e);
        } catch (Exception e) {
            throw new RuntimeException("Could not get hbase admin : "+e);
        }
    }

    @Override
    public void startCluster() {
        //this is a no-op operation as there is nothing like starting remote hbase. If it's not running already we can't help
    }

    @Override
    public void stopCluster() {
        //similar to above comment
    }

    @Override
    public void createTable() {
        try {
            if(!hBaseAdmin.tableExists(hbaseTestConfig.getTableName().getBytes())) {
                HTableDescriptor descriptor = new HTableDescriptor(hbaseTestConfig.getTableName());
                for (String colFamily : hbaseTestConfig.columnFamilies())
                    descriptor.addFamily(new HColumnDescriptor(colFamily));
                hBaseAdmin.createTable(descriptor);
            } else if (hBaseAdmin.isTableDisabled(hbaseTestConfig.getTableName().getBytes()))
                hBaseAdmin.enableTable(hbaseTestConfig.getTableName());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new hbase table "+hbaseTestConfig.getTableName()+" ", e);
        }
    }

    private HBaseConfiguration getConfiguration() {
        try {
            if (hBaseConfiguration == null) {
                Configuration configuration = HBaseConfiguration.create();
                hbaseTestConfig.getHbaseSiteConfig().forEach(configuration::set);
                hBaseConfiguration = new HBaseConfiguration(configuration);
                return hBaseConfiguration;
            }
            return hBaseConfiguration;
        }catch (Exception e){
            throw new RuntimeException("Could not get Hbase configuration : "+e);
        }
    }


    @Override
    public HTable getTable() {
        try {
            return new HTable(hBaseConfiguration,hbaseTestConfig.getTableName());
        } catch (Exception e) {
            throw new RuntimeException("Could not get table "+hbaseTestConfig.getTableName()+" "+e);
        }
    }

    @Override
    public void deleteTable() {
        try {
            if(hBaseAdmin.tableExists(hbaseTestConfig.getTableName())){
                hBaseAdmin.disableTable(hbaseTestConfig.getTableName());
                hBaseAdmin.deleteTable(hbaseTestConfig.getTableName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete the table " + hbaseTestConfig.getTableName(), e);
        }
    }

    @Override
    public void deleteAllTables(){
        try{
            Arrays.asList(hBaseAdmin.listTables(Pattern.compile("regression_.*"))).forEach(table->{
                if(table.getNameAsString().contains("regression_")){
                    try {
                        if(hBaseAdmin.isTableEnabled(table.getName()))
                            hBaseAdmin.disableTable(table.getName());
                        hBaseAdmin.deleteTable(table.getName());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
