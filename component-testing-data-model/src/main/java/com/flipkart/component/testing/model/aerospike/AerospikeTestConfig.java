package com.flipkart.component.testing.model.aerospike;

import com.flipkart.component.testing.model.TestConfig;

import java.util.List;

public interface AerospikeTestConfig extends TestConfig {
    String getHost();
    int getPort();
    String getUser();
    String getPassword();
    List<AerospikeData> getAerospikeData();

}
