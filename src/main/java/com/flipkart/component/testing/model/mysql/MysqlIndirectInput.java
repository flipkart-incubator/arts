package com.flipkart.component.testing.model.mysql;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.shared.MysqlConnectionConfig;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@JsonTypeName("mysqlIndirectInput")
public class MysqlIndirectInput implements IndirectInput, MysqlConnectionConfig {

    /**
     * type of connection to establish : Hsql(in memory) or mysql(localhost)
     * values : memory, localhost
     */
    private MysqlConnectionType connectionType;

    /**
     * databaseName
     */
    private String databaseName;

    /**
     * DDL Statements to execute on mysql database
     * for example : "CREATE TABLE `ListingScoreData` (`listingId` varchar(32) NOT NULL,`listingScoreData` text NOT NULL,`updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,PRIMARY KEY (`listingId`)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
     */
    private List<String> ddlStatements;

    /**
     * map which has the information of tables and corresponding tablesData
     * {
     *     t1:[
     *     {"c1": "v1"}, //row of t1
     *     ],
     *     t2:[
     *      {"c2": "v2"} // row of t2
     *     ]
     * }
     */
    private Map<String, List<Map<String, Object>>> tablesData;


    @JsonCreator
    public MysqlIndirectInput(
            @JsonProperty("databaseName") String databaseName,
            @JsonProperty("ddlStatements") List<String> ddlStatements,
            @JsonProperty("tablesData") Map<String,List<Map<String, Object>>> tablesData,
            @JsonProperty("connectionType") MysqlConnectionType connectionType

    ) {
        this.databaseName = databaseName;
        this.ddlStatements = ddlStatements;
        this.tablesData = tablesData;
        this.connectionType = connectionType;
    }

    public MysqlIndirectInput(
            String databaseName,
            List<String> ddlStatements,
            Map<String,List<Map<String, Object>>> tablesData
    ) {
        this(databaseName,ddlStatements,tablesData,MysqlConnectionType.IN_MEMORY);
    }

}
