package com.flipkart.component.testing;

import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.DataType;

import java.util.*;

class JSONDataSet extends AbstractDataSet {

    private final Map<String, List<Map<String, Object>>> map;

    JSONDataSet(Map<String, List<Map<String, Object>>> map) {
        this.map = map;
    }


    /**
     * Creates an iterator which provides access to all tables of this dataset
     *
     * @param reversed Whether the created iterator should be a reversed one or not
     * @return The created {@link ITableIterator}
     * @throws DataSetException
     */
    @Override
    protected ITableIterator createIterator(boolean reversed) throws DataSetException {
        ITable[] tables = this._getTables();
        if (reversed) {
            ITable[] tables1 = new ITable[tables.length];
            int index = 0;
            for (int i = tables.length; i >= 0; i--) {
                tables1[index++] = tables[i];
            }
            tables = tables1;
        }
        return new DefaultTableIterator(tables);
    }


    /**
     * Returns the specified table.
     *
     * @param tableName
     * @throws NoSuchTableException if dataset do not contains the specified
     *                              table
     */
    @SuppressWarnings("unchecked")
    private ITable _getTable(String tableName) throws DataSetException {

        // get the rows for the table
        List<Map<String, Object>> rows = map.get(tableName);

        ITableMetaData meta = this._getTableMetaData(tableName);
        // create a table based on the metadata
        DefaultTable table = new DefaultTable(meta);
        int rowIndex = 0;
        // iterate through the rows and fill the table
        for (Map<String, Object> row : rows) {
            fillRow(table, row, rowIndex++);
        }

        return table;
        // add the table to the list of DBUnit tables
    }

    /**
     * Returns tables in this dataset in proper sequence. Multiple tables having
     * the same name but different data may be returned.
     */
    private ITable[] _getTables() throws DataSetException {
        List<ITable> tables = new ArrayList<>();
        for (Map.Entry<String, List<Map<String, Object>>> entry : map.entrySet()) {
            ITable table = _getTable(entry.getKey());
            tables.add(table);
        }

        return tables.toArray(new ITable[]{});
    }

    /**
     * Fill a table row
     *
     * @param table    The table to be filled
     * @param row      A map containing the column values
     * @param rowIndex The index of the row to te filled
     */
    private void fillRow(DefaultTable table, Map<String, Object> row, int rowIndex) {
        try {
            table.addRow();
            // set the column values for the current row
            for (Map.Entry<String, Object> column : row.entrySet()) {
                table.setValue(rowIndex, column.getKey(), column.getValue());

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Returns the specified table metadata.
     *
     * @param tableName
     */
    private ITableMetaData _getTableMetaData(String tableName) throws DataSetException {
        Set<String> columns = new LinkedHashSet<>();
        List<Map<String, Object>> rows = (List<Map<String, Object>>) this.map.get(tableName);

        // iterate through the dataset and add the column names to a set
        for (Map<String, Object> row : rows) {
            for (Map.Entry<String, Object> column : row.entrySet()) {
                columns.add(column.getKey());
            }
        }
        List list = new ArrayList(columns.size());
        // create a list of DBUnit columns based on the column name set
        for (String s : columns) {
            list.add(new Column(s, DataType.UNKNOWN));
        }
        return new DefaultTableMetaData(tableName, (Column[]) list.toArray(new Column[list.size()]));
    }


}
