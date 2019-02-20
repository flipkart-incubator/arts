package com.flipkart.component.testing.model.solr;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.shared.SolrTestConfig;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Elastic search Indirect Input
 */
@JsonTypeName("solrObservation")
@Getter
public class SolrObservation implements Observation,SolrTestConfig {

    @Getter
    private final SolrExpectedData solrExpectedData;
    private ConnectionType connectionType;

    @JsonCreator
    public SolrObservation(@JsonProperty("solrExpectedData") SolrExpectedData solrExpectedData,
                            @JsonProperty("connectionType") ConnectionType connectionType
    ) {
        this.solrExpectedData = solrExpectedData;
        this.connectionType= connectionType;
    }


    @Override
    public String getSolrCoreName() {
        return solrExpectedData.getCoreName();
    }

    @Override
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Override
    public String getConfigFilePath() {
        return null;
    }
    @Data
    public static class SolrExpectedData {
        @Getter
        private String coreName;
        private List<Map<String,String>> documents;
        private String queryFile;

        @JsonCreator
        public SolrExpectedData(@JsonProperty("coreName") String coreName,
                                @JsonProperty("queryFile") String queryFile,
                                @JsonProperty("documents") List<Map<String, String>> documents){
            this.coreName= coreName;
            this.documents= documents;
            this.queryFile= queryFile;
        }

    }
}

