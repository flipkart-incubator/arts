package com.flipkart.component.testing.servers;

import com.aerospike.client.*;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.flipkart.component.testing.model.aerospike.AerospikeIndirectInput;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

class AerospikeServer implements DependencyInitializer{
    AerospikeClient client = null;
    AerospikeIndirectInput aerospikeIndirectInput;

    public AerospikeServer(AerospikeIndirectInput aerospikeIndirectInput){
        this.aerospikeIndirectInput = aerospikeIndirectInput;
    }

    @Override
    public void initialize(){

    }

    @Override
    public void shutDown() {
        deleteData();
        client.close();
    }

    @Override
    public void clean() {
        throw new NotImplementedException();
    }

    private void deleteData(){


        client = new AerospikeClient(aerospikeIndirectInput.getData().getHost(),
                aerospikeIndirectInput.getData().getPort());

        ScanPolicy scanPolicy = new ScanPolicy();

        client.scanAll(scanPolicy, aerospikeIndirectInput.getData().getNamespace(),
                aerospikeIndirectInput.getData().getSet(), new ScanCallback() {

                    public void scanCallback(Key key, Record record) throws AerospikeException {
                        client.delete(new WritePolicy(), key);
                    }
                });
    }
}
