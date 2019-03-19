package com.flipkart.component.testing.extractors;

import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
import com.flipkart.component.testing.model.aerospike.AerospikeData;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;
import com.flipkart.component.testing.shared.AerospikeOperations;
import com.flipkart.component.testing.shared.ObjectFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AerospikeLocalConsumer implements ObservationCollector<AerospikeObservation> {
    AerospikeOperations aerospikeOperations;
    @Override
    public AerospikeObservation actualObservations(AerospikeObservation expectedObservation){
        this.aerospikeOperations = ObjectFactory.getAerospikeOperations(expectedObservation);

        List<AerospikeData> aerospikeDataList = new ArrayList<>();
        int aerospikeDataCount=0;

        for(AerospikeData aerospikeObservationData :expectedObservation.getAerospikeData()){
            aerospikeDataList.add(new AerospikeData(
                    aerospikeObservationData.getNamespace(),aerospikeObservationData.getSet(),
                    getListOfRecords(expectedObservation,aerospikeDataCount)));
            aerospikeDataCount++;
        }
        return new AerospikeObservation(
                new AerospikeObservation.AerospikeConnectionInfo(expectedObservation.getConnectionInfo().getHostIP(),expectedObservation.getConnectionInfo().getPort())
                , aerospikeDataList);
    }

    List<AerospikeData.AerospikeRecords> getListOfRecords(AerospikeObservation aerospikeObservation, int aerospikeDataCount){
        Statement statement = null;
        List<AerospikeData.AerospikeRecords> recordList = new ArrayList();
        for(AerospikeData.AerospikeRecords aerospikeRecord: aerospikeObservation.getAerospikeData().get(aerospikeDataCount).getRecords()) {
            String primaryKey= null;
            Map<String,Object> binDataMap= null;
            statement = new Statement();
            statement.setNamespace(aerospikeObservation.getAerospikeData().get(aerospikeDataCount).getNamespace());
            statement.setSetName(aerospikeObservation.getAerospikeData().get(aerospikeDataCount).getSet());
            RecordSet recordSet = aerospikeOperations.getClient().query(null, statement);
            try {
                while (recordSet.next()) {
                    if(recordSet.getKey().userKey!=null){
                        if(!aerospikeRecord.getPrimaryKey().toString().equalsIgnoreCase(recordSet.getKey().userKey.toString()))
                            continue;
                        primaryKey= recordSet.getKey().userKey.toString();
                    }
                    else
                        primaryKey="";
                    binDataMap = new HashMap<>();
                    for(String binkey : aerospikeRecord.getBinData().keySet()) {
                        binDataMap.put(binkey, recordSet.getRecord().getValue(binkey));
                    }
                }
            } finally {
                recordSet.close();
            }
            recordList.add(new AerospikeData.AerospikeRecords(primaryKey,binDataMap));
        }
        return recordList;
    }

}

