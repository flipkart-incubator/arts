package com.flipkart.component.testing.loaders;

import com.flipkart.component.testing.model.aerospike.AerospikeData;
import com.flipkart.component.testing.model.aerospike.AerospikeIndirectInput;
import com.flipkart.component.testing.shared.AerospikeOperations;
import com.flipkart.component.testing.shared.ObjectFactory;


class AerospikeDataLoader implements TestDataLoader<AerospikeIndirectInput>{

    @Override
    public void load(AerospikeIndirectInput indirectInput){
        AerospikeOperations aerospikeOperations = ObjectFactory.getAerospikeOperations(indirectInput);
        for (AerospikeData aerospikeData:indirectInput.getAerospikeData()){
            for(AerospikeData.AerospikeRecords record : aerospikeData.getRecords()){
                for( String binKey: record.getBinData().keySet()){
                    aerospikeOperations.getClient().put(
                                    aerospikeOperations.getWritePolicy(),
                                    aerospikeOperations.getNewPrimaryKey(aerospikeData.getNamespace(),
                                            aerospikeData.getSet(),record.getPrimaryKey()),
                                    aerospikeOperations.getNewBin(binKey,record.getBinData().get(binKey)));
                }
            }
        }
    }
}

