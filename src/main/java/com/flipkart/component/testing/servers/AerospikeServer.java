package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.shared.AerospikeTestConfig;
import com.flipkart.component.testing.shared.ObjectFactory;

class AerospikeServer implements DependencyInitializer{
    private final AerospikeTestConfig aerospikeTestConfig;

    public AerospikeServer(AerospikeTestConfig aerospikeTestConfig){
        this.aerospikeTestConfig= aerospikeTestConfig;
    }
    @Override
    public void initialize(){

    }

    @Override
    public void shutDown() {
        ObjectFactory.getAerospikeOperations(this.aerospikeTestConfig).clean();
        ObjectFactory.getAerospikeOperations(this.aerospikeTestConfig).closeClient();
    }

    @Override
    public void clean() {

    }

}
