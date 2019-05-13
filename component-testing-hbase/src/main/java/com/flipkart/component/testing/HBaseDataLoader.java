package com.flipkart.component.testing;

import com.flipkart.component.testing.model.hbase.HBaseIndirectInput;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HBaseDataLoader implements TestDataLoader<HBaseIndirectInput> {

	/**
	 * loads the hbaseIndirectInput(tableName, Rows) into Hbase
	 * 
	 * @param hBaseIndirectInput
	 */
	@Override
	public void load(HBaseIndirectInput hBaseIndirectInput) {
		HBaseAdminOperations hBaseOperations = HbaseFactory.getHBaseOperations(hBaseIndirectInput);
		HTableInterface table = hBaseOperations.getTable(hBaseIndirectInput);

		Put p;
		for (int row = 0; row < hBaseIndirectInput.getRows().size(); row++) {
			p = new Put(Bytes.toBytes(hBaseIndirectInput.getRows().get(row).getRowKey()));
			for (String colFamily : hBaseIndirectInput.getRows().get(row).getData().keySet()) {
				Map<String, String> qualifierMap = hBaseIndirectInput.getRows().get(row).getData().get(colFamily);
				if(qualifierMap.isEmpty()) return;
				for (String qualifier : qualifierMap.keySet()) {
					String qualValue = qualifierMap.get(qualifier);
					p.add(Bytes.toBytes(colFamily), Bytes.toBytes(qualifier), Bytes.toBytes(qualValue));
				}
			}
			try {
				table.put(p);
			} catch (Exception e) {
				throw new RuntimeException("Could not insert data into table : " + hBaseIndirectInput.getTableName()
						+ " \n" + e.getMessage());
			}
		}

	}

	/**
	 * returns the indirectInput class associated with this loader.
	 */
	@Override
	public List<Class<HBaseIndirectInput>> getIndirectInputClasses() {
		return Arrays.asList(HBaseIndirectInput.class);
	}

}