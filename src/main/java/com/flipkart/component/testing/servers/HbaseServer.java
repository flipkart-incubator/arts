package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.shared.HBaseAdminOperations;
import com.flipkart.component.testing.shared.HbaseTestConfig;
import com.flipkart.component.testing.shared.ObjectFactory;

class HbaseServer implements DependencyInitializer {

    private final HbaseTestConfig hbaseTestConfig;
    private HBaseAdminOperations hBaseAdminOperations;

    HbaseServer(HbaseTestConfig hbaseTestConfig) {
        this.hBaseAdminOperations = ObjectFactory.getHBaseOperations(hbaseTestConfig);
        this.hbaseTestConfig = hbaseTestConfig;
    }

    /**
     * Starts the hbase cluster locally
     */
    @Override
    public void initialize() {
        this.hBaseAdminOperations.startCluster();

        if(this.hbaseTestConfig.shouldCreateTable()){
            this.hBaseAdminOperations.createTable(this.hbaseTestConfig);
        }

    }

    /**
     * Shuts down the hbase cluster
     */
    @Override
    public void shutDown() {
        this.hBaseAdminOperations.deleteTable(this.hbaseTestConfig);
        this.hBaseAdminOperations.stopCluster();
    }

    @Override
    public void clean() {
        this.hBaseAdminOperations.deleteTable(this.hbaseTestConfig);
    }
}
