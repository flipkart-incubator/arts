package com.flipkart.component.testing.internal;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONDataSetTest {


    private static JSONDataSet jsonDataSet;

    @BeforeClass
    public static void setUp() {
        Map<String, List<Map<String,Object>>> tablesData = new HashMap<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> rowValues = new HashMap<>();
        rowValues.put("a", "b");
        rows.add(rowValues);
        tablesData.put("tableName1", rows);
        tablesData.put("tableName2", rows);
        jsonDataSet = new JSONDataSet(tablesData);
    }

    @Test
    public void testGetTableNames() throws DataSetException {
        List<String> tableNames = Arrays.asList(jsonDataSet.getTableNames());
        assertEquals(tableNames.size(), 2);
        assertTrue(tableNames.contains("tableName1"));
        assertTrue(tableNames.contains("tableName2"));

    }

    @Test
    public void testGetTableMeta() throws DataSetException {
        ITableMetaData tableName1 = jsonDataSet.getTableMetaData("tableName1");
        assertEquals("tableName1", tableName1.getTableName());
        Column[] columns = tableName1.getColumns();
        assertEquals(1, columns.length);
        assertEquals("a", columns[0].getColumnName());
    }

    @Test
    public void testGetTable() throws DataSetException {
        ITable tableName1 = jsonDataSet.getTable("tableName1");
        assertEquals(1, tableName1.getRowCount());
        assertEquals("b", tableName1.getValue(0, "a"));
    }

    @Test
    public void testGetTables() throws DataSetException {
        ITable[] tables = jsonDataSet.getTables();
        assertEquals(2, tables.length);
    }
}
