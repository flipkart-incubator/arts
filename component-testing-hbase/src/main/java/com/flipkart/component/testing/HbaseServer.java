package com.flipkart.component.testing;

import com.flipkart.component.testing.model.hbase.HBaseIndirectInput;
import com.flipkart.component.testing.model.hbase.HBaseObservation;
import com.flipkart.component.testing.model.hbase.HbaseTestConfig;

class HbaseServer implements DependencyInitializer<HBaseIndirectInput, HBaseObservation, HbaseTestConfig> {

    private HbaseTestConfig hbaseTestConfig;
    private HBaseAdminOperations hBaseAdminOperations;


    @Override
    public void initialize(HbaseTestConfig testConfig) throws Exception {
        this.hbaseTestConfig = testConfig;
        this.hBaseAdminOperations = HbaseFactory.getHBaseOperations(hbaseTestConfig);
        this.hBaseAdminOperations.startCluster();
    }

    /**
     * Shuts down the hbase cluster
     */
    @Override
    public void shutDown() {
        this.hBaseAdminOperations.deleteAllTables(this.hbaseTestConfig);
        this.hBaseAdminOperations.stopCluster();
    }

    @Override
    public void clean() {
        this.hBaseAdminOperations.deleteAllTables(this.hbaseTestConfig);
    }

    @Override
    public Class<HBaseIndirectInput> getIndirectInputClass() {
        return HBaseIndirectInput.class;
    }

    @Override
    public Class<HBaseObservation> getObservationClass() {
        return HBaseObservation.class;
    }
}
