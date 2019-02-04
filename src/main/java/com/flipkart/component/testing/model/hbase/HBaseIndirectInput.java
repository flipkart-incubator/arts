package com.flipkart.component.testing.model.hbase;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.shared.HbaseTestConfig;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

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

    @JsonCreator
    public HBaseIndirectInput(@JsonProperty("tableName") String tableName,
                              @JsonProperty("rows") List<Row> rows,
                              @JsonProperty("connectionType") ConnectionType connectionType,
                              @JsonProperty("hbaseSiteConfig") Map<String,String> hbaseSiteConfig) {

        this.tableName = tableName;
        this.rows = rows;
        this.connectionType = connectionType;
        this.hbaseSiteConfig = hbaseSiteConfig;
    }

    @Override
    @JsonIgnore
    public String[] columnFamilies() {
        if (this.getRows() != null && !this.getRows().isEmpty()) {
            int numberOfColFamilies = this.getRows().get(0).getData().keySet().size();
            String[] colFamilies = this.getRows().get(0).getData().keySet()
                    .toArray(new String[numberOfColFamilies]);
            return colFamilies;
        }
        return new String[0];
    }

    @Override
    @JsonIgnore
    public boolean shouldCreateTable() {
        return true;
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
        public Row(@JsonProperty("rowKey") String rowKey,
                   @JsonProperty("data") Map<String, Map<String, String>> data) {
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
        private Map<String, Map<String, String>> data;
    }
}
