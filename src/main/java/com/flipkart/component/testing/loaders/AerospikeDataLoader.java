package com.flipkart.component.testing.loaders;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.*;
import com.flipkart.component.testing.model.AerospikeIndirectInput;

import java.util.List;
import java.util.Map;


class AerospikeDataLoader implements TestDataLoader<AerospikeIndirectInput>{


    @Override
    public void load(AerospikeIndirectInput indirectInput){

        Host[] hosts = new Host[] {
                new Host(indirectInput.getData().getHost(),indirectInput.getData().getPort()),
        };
        WritePolicy policy = new WritePolicy();
        AerospikeClient client;
        client = new AerospikeClient(new ClientPolicy(),hosts);
        policy.sendKey = true;

        List<Map<String,Map<String,String>>> datalist = indirectInput.getData().getRecords();

        for(Map<String,Map<String,String>> record : datalist){
            for(Map.Entry<String,String> entry: record.get(record.keySet().iterator().next()).entrySet()){
                Key key = new Key(indirectInput.getData().getNamespace(), indirectInput.getData().getSet(),
                        record.keySet().iterator().next() );
                Bin aerospikeBin = new Bin(entry.getKey(), entry.getValue());
                client.put(policy, key, aerospikeBin);
            }
        }
    }
}

