package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.model.aerospike.AerospikeData;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;

import java.util.List;

public interface AerospikeTestConfig {
    String getHost();
    int getPort();
    List<AerospikeData> getAerospikeData();

}
