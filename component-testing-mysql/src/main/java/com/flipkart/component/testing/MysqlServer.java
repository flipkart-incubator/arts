package com.flipkart.component.testing;

import com.flipkart.component.testing.model.mysql.MysqlIndirectInput;
import com.flipkart.component.testing.model.mysql.MysqlObservation;
import com.flipkart.component.testing.model.mysql.MysqlTestConfig;


class MysqlServer implements DependencyInitializer<MysqlIndirectInput,MysqlObservation,MysqlTestConfig>{

    private MysqlTestConfig testConfig;

    @Override
    public void initialize(MysqlTestConfig testConfig) {
        this.testConfig = testConfig;
    }

    @Override
    public void shutDown() {
        MysqlFactory.getMysqlConnection(testConfig).dropDatabase(testConfig.getDatabaseName());
    }

    @Override
    public void clean() {
        MysqlFactory.getMysqlConnection(testConfig).dropDatabase(testConfig.getDatabaseName());
    }

    @Override
    public Class<MysqlIndirectInput> getIndirectInputClass() {
        return MysqlIndirectInput.class;
    }

    @Override
    public Class<MysqlObservation> getObservationClass() {
        return MysqlObservation.class;
    }


}
