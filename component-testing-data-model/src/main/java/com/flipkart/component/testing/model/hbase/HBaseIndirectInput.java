package com.flipkart.component.testing.model.hbase;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.IndirectInput;
import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Hbase Indirect Input
 */
@JsonTypeName("hbaseIndirectInput")
@Getter
public class HBaseIndirectInput implements IndirectInput, HbaseTestConfig {

    /**
     * The name of the table to load the data to
     */
    private final String tableName;

    /**
     * list of rows which needs to be loaded to hbase
     */
    private final List<Row> rows;

    /**
     * connection type for hbase
     *
     * @param tableName
     * @param rows
     */
    private ConnectionType connectionType;


    /**
     * file path of hbaseSiteXMl
     */
    private Map<String,String> hbaseSiteConfig;
    private String inputFile;

    @JsonCreator
    public HBaseIndirectInput(@JsonProperty("tableName") String tableName,
                               @JsonProperty("rows") List<Row> rows,
                               @JsonProperty("connectionType") ConnectionType connectionType,
                               @JsonProperty("hbaseSiteConfig") Map<String, String> hbaseSiteConfig,
                               @JsonProperty("hbaseIndirectInputFile") String inputFile) {

        this.tableName = tableName;
        this.rows = rows;
        this.connectionType = connectionType;
        this.hbaseSiteConfig = hbaseSiteConfig;
        this.inputFile = inputFile;
    }

    public String getTableName() {
        if(tableName!=null && !tableName.contains("regression_"))
            throw new RuntimeException("The table name used is not in correct format for testing. Try prefix 'regression_' ");
        else return tableName;
    }

    @Override
    @JsonIgnore
    public Set<String> columnFamilies() {
        if (this.getRows() != null && !this.getRows().isEmpty()) {
            Set<String> cfNames = new HashSet<>();
            this.getRows().forEach(row -> cfNames.addAll(row.getData().keySet()));
            return cfNames;
        }
        return new HashSet<>();
    }


    /**
     * config for indirect input to whether load before or after SUT start
     *
     * @return
     */
    @Override
    public boolean isLoadAfterSUT() {
        return false;
    }

    @Data
    public static class Row {


        @JsonCreator
        Row(@JsonProperty("rowKey") String rowKey,
            @JsonProperty("data") Map<String, Map<String, Object>> data) {
            this.rowKey = rowKey;
            this.data = data;
        }

        /**
         * the rowkey for the row
         */
        private String rowKey;

        /**
         * columnFamily => (columnName => columnValue)
         */
        private Map<String, Map<String, Object>> data;
    }
}
