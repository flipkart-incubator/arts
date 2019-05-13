package com.flipkart.component.testing;

import com.flipkart.component.testing.model.aerospike.AerospikeData;
import com.flipkart.component.testing.model.aerospike.AerospikeIndirectInput;

import java.util.Arrays;
import java.util.List;


class AerospikeDataLoader implements TestDataLoader<AerospikeIndirectInput>{

    @Override
    public void load(AerospikeIndirectInput indirectInput){
        AerospikeOperations aerospikeOperations = RemoteAerospikeOperations.getInstance(indirectInput);
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

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<AerospikeIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(AerospikeIndirectInput.class);
    }
}

