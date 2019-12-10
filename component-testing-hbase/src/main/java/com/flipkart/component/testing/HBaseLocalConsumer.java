package com.flipkart.component.testing;

import com.flipkart.component.testing.model.hbase.HBaseObservation;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.*;

/**
 * Reads from the local Hbase Consumer
 */
class HBaseLocalConsumer implements ObservationCollector<HBaseObservation> {
    /**
     * extracts the actual Observation using attributes from expected observation
     * like tableName
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public HBaseObservation actualObservations(HBaseObservation expectedObservation) {
        List<HBaseObservation.Row> rows = this.getRows(expectedObservation);
        return new HBaseObservation(expectedObservation.getTableName(), rows, expectedObservation.getConnectionType(), expectedObservation.getHbaseSiteConfig());
    }

    @Override
    public Class<HBaseObservation> getObservationClass() {
        return HBaseObservation.class;
    }

    private List<HBaseObservation.Row> getRows(HBaseObservation expectedObservation) {
        List<HBaseObservation.Row> rows = new ArrayList<>();
        Table table = HbaseFactory.getHBaseOperations(expectedObservation).getTable(expectedObservation);
        try {
            for (Result rowResult : table.getScanner(new Scan())) {
                Map<String, Map<String, Object>> actualDataMap = new HashMap<>();
                table.get(new Get(rowResult.getRow())).getMap().forEach((columnFamilyName, qualifierMap)->{
                    Map<String, Object> actualQualifierMap = new HashMap<>();
                    qualifierMap.forEach((QKey, QValue) -> actualQualifierMap.put(Bytes.toString(QKey),
                            Bytes.toString((byte[]) ((TreeMap) QValue).get(((TreeMap) QValue).keySet().iterator().next())))
                    );
                    actualDataMap.put(Bytes.toString(columnFamilyName), actualQualifierMap);
                });
                rows.add(new HBaseObservation.Row(Bytes.toString(rowResult.getRow()),actualDataMap));
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not return a scanner on table : " + expectedObservation.getTableName()
                    + " \n" + e.getMessage());
        }
        return rows;
    }
}
