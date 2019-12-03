package com.flipkart.component.testing;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.flipkart.component.testing.model.aerospike.AerospikeData;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AerospikeLocalConsumer implements ObservationCollector<AerospikeObservation> {


    private AerospikeOperations aerospikeOperations;


    @Override
    public AerospikeObservation actualObservations(AerospikeObservation expectedObservation){
        this.aerospikeOperations = RemoteAerospikeOperations.getInstance(expectedObservation);

        List<AerospikeData> aerospikeDataList = new ArrayList<>();
        int aerospikeDataCount=0;

        for(AerospikeData aerospikeObservationData :expectedObservation.getAerospikeData()){
            aerospikeDataList.add(new AerospikeData(
                    aerospikeObservationData.getNamespace(),aerospikeObservationData.getSet(),
                    getListOfRecords(expectedObservation,aerospikeDataCount)));
            aerospikeDataCount++;
        }
        return new AerospikeObservation(
                new AerospikeObservation.AerospikeConnectionInfo(expectedObservation.getConnectionInfo().getHostIP(),expectedObservation.getConnectionInfo().getPort(),expectedObservation.getConnectionInfo().getUser(),expectedObservation.getConnectionInfo().getPassword())
                , aerospikeDataList);
    }

    @Override
    public Class<AerospikeObservation> getObservationClass() {
        return AerospikeObservation.class;
    }

    private List<AerospikeData.AerospikeRecords> getListOfRecords(AerospikeObservation aerospikeObservation, int aerospikeDataCount) {
        List<AerospikeData.AerospikeRecords> recordList = new ArrayList<>();
        for(AerospikeData.AerospikeRecords aerospikeRecord: aerospikeObservation.getAerospikeData().get(aerospikeDataCount).getRecords()) {
            Key key = new Key(aerospikeObservation.getAerospikeData().get(aerospikeDataCount).getNamespace(),
                    aerospikeObservation.getAerospikeData().get(aerospikeDataCount).getSet(),
                    aerospikeRecord.getPrimaryKey());
            Record record = aerospikeOperations.getClient().get(null , key);
            Map<String,Object> binDataMap = new HashMap<>();
            for (String binKey  : aerospikeRecord.getBinData().keySet()) {
                binDataMap.put(binKey, record.getValue(binKey));
            }
            recordList.add(new AerospikeData.AerospikeRecords(aerospikeRecord.getPrimaryKey(), binDataMap));
        }
        return recordList;
    }

}

