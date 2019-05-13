package com.flipkart.component.testing.model.solr;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.IndirectInput;
import lombok.Getter;

/**
 * Elastic search Indirect Input
 */
@JsonTypeName("solrIndirectInput")
@Getter
public class SolrIndirectInput implements IndirectInput,SolrTestConfig {

    @Getter
    private final SolrData solrData;
    private ConnectionType connectionType;

    @JsonCreator
    public SolrIndirectInput(@JsonProperty("solrData") SolrData solrData,
                             @JsonProperty("connectionType")ConnectionType connectionType
    ) {
        this.solrData = solrData;
        this.connectionType = connectionType;

    }


    @Override
    public String getSolrCoreName() {
        return solrData.getCoreName();
    }

    @Override
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Override
    public String getConfigFilePath() {
        return getSolrData().getSolrConfigFiles();
    }

    @Override
    public boolean isLoadAfterSUT() {
        return false;
    }
}

