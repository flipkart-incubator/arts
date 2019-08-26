package com.flipkart.component.testing.model.mysql;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import lombok.AccessLevel;
import lombok.Getter;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@JsonTypeName("mysqlObservation")
@Getter
public class MysqlObservation implements Observation, MysqlTestConfig {


    /**
     * map that defines the data in tables and rows
     * tableName => list of rows
     * row => map of column and value
     */
    @Getter(AccessLevel.PRIVATE)
    private Map<String, ITable> data = new HashMap<>();

    /**
     * the database from we need to observe
     */
    private String databaseName;


    private ConnectionInfo connectionInfo;

    /**
     * Used to define the observation from mysql
     *
     * @param expectedObservation
     * @param databaseName
     * @throws FileNotFoundException
     * @throws DataSetException
     */
    @JsonCreator
    public MysqlObservation(@JsonProperty("data") Map<String, List<Map<String, Object>>> expectedObservation,
                            @JsonProperty("databaseName") String databaseName,
                            @JsonProperty("connectionInfo")ConnectionInfo connectionInfo) throws DataSetException {
        IDataSet dataSet = new JSONDataSet(expectedObservation);
        for (String tableName : dataSet.getTableNames()) {
            data.put(tableName.toUpperCase(), dataSet.getTable(tableName));
        }
        this.databaseName = databaseName;
        this.connectionInfo = connectionInfo;
    }

    private MysqlObservation(){}

    public static MysqlObservation with(Map<String, ITable> data, String databaseName) {
        Map<String, ITable> upperCaseTableNameMap = data.keySet().stream().collect(Collectors.toMap(String::toUpperCase, data::get));
        MysqlObservation mysqlObservation = new MysqlObservation();
        mysqlObservation.databaseName = databaseName;
        mysqlObservation.data = upperCaseTableNameMap;
        return mysqlObservation;
    }
    /**
     * return the table
     *
     * @param tableName
     * @return
     */
    @JsonIgnore
    public ITable getTable(String tableName) {
        tableName = tableName.toUpperCase();
        return this.data.get(tableName);
    }

    @JsonIgnore
    public Set<String> getTableNames() {
        return this.data.keySet();
    }

}
