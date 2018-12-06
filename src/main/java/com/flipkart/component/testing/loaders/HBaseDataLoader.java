package com.flipkart.component.testing.loaders;

import com.flipkart.component.testing.model.hbase.HBaseIndirectInput;
import com.flipkart.component.testing.shared.HBaseAdminOperations;
import com.flipkart.component.testing.shared.ObjectFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseDataLoader implements TestDataLoader<HBaseIndirectInput> {

	/**
	 * loads the hbaseIndirectInput(tableName, Rows) into Hbase
	 * 
	 * @param hBaseIndirectInput
	 */
	@Override
	public void load(HBaseIndirectInput hBaseIndirectInput) {
		HBaseAdminOperations hBaseOperations = ObjectFactory.getHBaseOperations(hBaseIndirectInput);
		HTableInterface table = hBaseOperations.getTable(hBaseIndirectInput);

		Put p;
		for (int row = 0; row < hBaseIndirectInput.getRows().size(); row++) {
			p = new Put(Bytes.toBytes(hBaseIndirectInput.getRows().get(row).getRowKey()));
			for (String colFamily : hBaseIndirectInput.getRows().get(row).getData().keySet()) {
				for (String qualifier : hBaseIndirectInput.getRows().get(row).getData().get(colFamily).keySet()) {
					String qualValue = hBaseIndirectInput.getRows().get(row).getData().get(colFamily).get(qualifier);
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

}
