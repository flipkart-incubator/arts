package com.flipkart.component.testing.shared;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Priority;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.policy.WritePolicy;

public class RemoteAerospikeOperations implements AerospikeOperations,ScanCallback {
    private static RemoteAerospikeOperations instance = null;
    private AerospikeClient aerospikeClient;
    private AerospikeTestConfig aerospikeTestConfig;

    private RemoteAerospikeOperations(AerospikeTestConfig aerospikeTestConfig) {
        this.aerospikeTestConfig = aerospikeTestConfig;
    }

    public static AerospikeOperations getInstance(AerospikeTestConfig aerospikeTestConfig) {
        if (instance==null)
            instance= new RemoteAerospikeOperations(aerospikeTestConfig);
        return instance;
    }

    @Override
    public AerospikeClient getClient() {
        this.aerospikeClient = new AerospikeClient(new ClientPolicy(),getClientHost());
        return aerospikeClient;
    }



    @Override
    public void closeClient() {
        aerospikeClient.close();
    }


    @Override
    public Key getNewPrimaryKey(String namesapce,String set,String key) {
        return new Key(namesapce, set, key);
    }

    @Override
    public Bin getNewBin(String binKey,Object binValue) {
        return new Bin(binKey,binValue);
    }

    @Override
    public Host getClientHost() {
        return new Host(aerospikeTestConfig.getHost(),
                aerospikeTestConfig.getPort());
    }

    @Override
    public WritePolicy getWritePolicy() {
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey= true;
        return writePolicy;
    }

    @Override
    public ScanPolicy getScanPolicy() {
        ScanPolicy scanPolicy = new ScanPolicy();
        scanPolicy.concurrentNodes = true;
        scanPolicy.priority = Priority.LOW;
        scanPolicy.includeBinData = true;
        scanPolicy.sendKey = true;
        return scanPolicy;
    }

    @Override
    public void clean() {

       try {
           for (int i = 0; i < aerospikeTestConfig.getAerospikeData().size(); i++) {
               getClient().scanAll(getScanPolicy(),
                       aerospikeTestConfig.getAerospikeData().get(i).getNamespace(),
                       aerospikeTestConfig.getAerospikeData().get(i).getSet(),this);
           }
       }catch (Exception e){
           throw new RuntimeException(e.getMessage());
       }
    }

    @Override
    public void scanCallback(Key key, Record record) throws AerospikeException {
        getClient().delete(new WritePolicy(),key);
    }
}
