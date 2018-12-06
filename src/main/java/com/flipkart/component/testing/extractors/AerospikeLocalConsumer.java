package com.flipkart.component.testing.extractors;

import com.aerospike.client.*;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Priority;
import com.aerospike.client.policy.ScanPolicy;
import com.flipkart.component.testing.model.aerospike.AerospikeData;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AerospikeLocalConsumer implements ObservationCollector<AerospikeObservation>, ScanCallback {

    // list of records(map) that will contain "keyname"(primary key) as key and a map as value
    // in which each key value pair will be fields and its value respectively.
    private List<Map<String,Map<String,String>>> datalist = new ArrayList<>();


    @Override
    public AerospikeObservation actualObservations(AerospikeObservation expectedObservation){

        this.scanData(expectedObservation);

        return new AerospikeObservation(new AerospikeData(expectedObservation.getData().getNamespace()
                ,expectedObservation.getData().getHost()
                ,expectedObservation.getData().getPort()
                ,expectedObservation.getData().getSet(),datalist));
    }

    private void scanData(AerospikeObservation expectedObservation) throws AerospikeException{
        AerospikeClient client = null;
        try{
            Host[] hosts = new Host[] {
                    new Host(expectedObservation.getData().getHost(),expectedObservation.getData().getPort()),
            };
            ScanPolicy policy = new ScanPolicy();
            policy.concurrentNodes = true;
            policy.priority = Priority.LOW;
            policy.includeBinData = true;
            policy.sendKey = true;
            client = new AerospikeClient(new ClientPolicy(),hosts);
            client.scanAll(policy,expectedObservation.getData().getNamespace(),
                    expectedObservation.getData().getSet(),this);
            try {
                //TODO: to remove temporary
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finally {
            if(client != null){
                client.close();
            }
        }
    }

    @Override
    public void scanCallback(Key key, Record record) throws AerospikeException {

        Map<String,String> binMap = new HashMap<>();
        for(Map.Entry<String,Object> entry:record.bins.entrySet()){
            binMap.put(entry.getKey(),entry.getValue().toString());
        }

        Map<String,Map<String, String>> recordMap = new HashMap<>();
        recordMap.put(key.userKey.toString(),binMap);
        datalist.add(recordMap);

    }
}

