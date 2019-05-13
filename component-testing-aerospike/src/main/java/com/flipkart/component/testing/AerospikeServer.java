package com.flipkart.component.testing;


import com.flipkart.component.testing.model.TestConfig;
import com.flipkart.component.testing.model.aerospike.AerospikeIndirectInput;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;
import com.flipkart.component.testing.model.aerospike.AerospikeTestConfig;

class AerospikeServer implements DependencyInitializer<AerospikeIndirectInput, AerospikeObservation, AerospikeTestConfig>{
    private AerospikeTestConfig aerospikeTestConfig;


    @Override
    public void initialize(AerospikeTestConfig testConfig) throws Exception {
        this.aerospikeTestConfig = testConfig;
    }

    @Override
    public void shutDown() {
        RemoteAerospikeOperations.getInstance(this.aerospikeTestConfig).clean();
        RemoteAerospikeOperations.getInstance(this.aerospikeTestConfig).closeClient();
    }

    @Override
    public void clean() {
        RemoteAerospikeOperations.getInstance(this.aerospikeTestConfig).clean();
    }

    @Override
    public Class<AerospikeIndirectInput> getIndirectInputClass() {
        return AerospikeIndirectInput.class;
    }

    @Override
    public Class<AerospikeObservation> getObservationClass() {
        return AerospikeObservation.class;
    }


}
