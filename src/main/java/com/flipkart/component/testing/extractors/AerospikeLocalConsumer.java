package com.flipkart.component.testing.extractors;

import com.aerospike.client.query.RecordSet;
import com.aerospike.client.query.Statement;
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

        List<AerospikeObservation.AerospikeObservationData> aerospikeDataList = new ArrayList<>();
        int aerospikeDataCount=0;

        for(AerospikeObservation.AerospikeObservationData aerospikeObservationData :expectedObservation.getData()){
            aerospikeDataList.add(new AerospikeObservation.AerospikeObservationData(
                    aerospikeObservationData.getNamespace(),aerospikeObservationData.getSet(),
                    getListOfRecords(expectedObservation,aerospikeDataCount)));
            aerospikeDataCount++;
        }
        return new AerospikeObservation(
                new AerospikeObservation.AerospikeConnectionInfo(expectedObservation.getConnectionInfo().getHostIP(),expectedObservation.getConnectionInfo().getPort())
                , aerospikeDataList);
    }

    List<AerospikeObservation.AerospikeObservationData.AerospikeRecords> getListOfRecords(AerospikeObservation aerospikeObservation, int aerospikeDataCount){
        Statement statement = null;
        List<AerospikeObservation.AerospikeObservationData.AerospikeRecords> recordList = new ArrayList();
        for(AerospikeObservation.AerospikeObservationData.AerospikeRecords aerospikeRecord: aerospikeObservation.getData().get(aerospikeDataCount).getRecords()) {
            String primaryKey= null;
            Map<String,Object> binDataMap= null;
            statement = new Statement();
            statement.setNamespace(aerospikeObservation.getData().get(aerospikeDataCount).getNamespace());
            statement.setSetName(aerospikeObservation.getData().get(aerospikeDataCount).getSet());
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
            recordList.add(new AerospikeObservation.AerospikeObservationData.AerospikeRecords(primaryKey,binDataMap));
        }
        return recordList;
    }

}

