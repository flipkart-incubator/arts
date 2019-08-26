package com.flipkart.component.testing;

import com.flipkart.component.testing.model.hbase.HBaseObservation;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<HBaseObservation.Row> rows = this.getRows(expectedObservation.getTableName(), expectedObservation);
        return new HBaseObservation(expectedObservation.getTableName(), rows, expectedObservation.getConnectionType(), expectedObservation.getHbaseSiteConfig());
    }

    @Override
    public Class<HBaseObservation> getObservationClass() {
        return HBaseObservation.class;
    }

    private List<HBaseObservation.Row> getRows(String tableName, HBaseObservation expectedObservation) {
        List<HBaseObservation.Row> rows = new ArrayList<>();
        Table table = HbaseFactory.getHBaseOperations(expectedObservation).getTable(expectedObservation);
        try {
            HColumnDescriptor[] columnDescriptors = table.getTableDescriptor().getColumnFamilies();
            for (Result rowResult : table.getScanner(new Scan())) {

                Map<String, Map<String, String>> map = new HashMap<>();
                for (HColumnDescriptor hColumnDescriptor : columnDescriptors) {
                    String colFamily = hColumnDescriptor.getNameAsString();
                    map.put(colFamily, new HashMap<>());
                    for (KeyValue q : rowResult.raw()) {
                        String val = Bytes.toString(rowResult.getValue(Bytes.toBytes(colFamily), q.getQualifier()));
                        map.get(colFamily).put(Bytes.toString(q.getQualifier()), val);
                        HBaseObservation.Row observationRow = new HBaseObservation.Row(Bytes.toString(rowResult.getRow()),
                                map);
                        rows.add(observationRow);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not return a scanner on table : " + expectedObservation.getTableName()
                    + " \n" + e.getMessage());
        }
        return rows;
    }
}
