package com.flipkart.component.testing.shared;


import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Host;
import com.aerospike.client.Key;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;

import java.util.List;

public interface AerospikeOperations {
    AerospikeClient getClient();
    void closeClient();
    Key getNewPrimaryKey(String namespace,String set,String key);
    Bin getNewBin(String binKey,Object binValue);
    Host getClientHost();
    WritePolicy getWritePolicy();
    ScanPolicy getScanPolicy();
    void clean();
}
