package com.flipkart.component.testing.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.Constants;
import com.flipkart.component.testing.shared.ElasticSearchTestConfig;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * Elastic search Indirect Input
 */
@JsonTypeName("elasticSearchIndirectInput")
@Getter
public class ElasticSearchIndirectInput implements IndirectInput, ElasticSearchTestConfig {

    @Getter(AccessLevel.PRIVATE)
    private final ElasticSearchConnectionInfo connectionInfo;
    private final List<DocumentsOfIndexAndType> documentsOfIndexAndType;

    @JsonCreator
    public ElasticSearchIndirectInput(@JsonProperty("connectionInfo") ElasticSearchConnectionInfo connectionInfo,
                                      @JsonProperty("documentsOfIndexAndType") List<DocumentsOfIndexAndType> documentsOfIndexAndType
    ) {
        this.connectionInfo = connectionInfo;
        this.documentsOfIndexAndType = documentsOfIndexAndType;
    }

    @Override
    public String getClusterName() {
        return Optional.ofNullable(connectionInfo.getClusterName()).orElse(Constants.ES_CLUSTER_NAME);
    }

    @Override
    public String getHost() {
        return Optional.ofNullable(connectionInfo.getHost()).orElse(Constants.ES_CLIENT_HOST);
    }

    @Override
    public ConnectionType getConnectionType() {
        return Optional.ofNullable(connectionInfo.getConnectionType()).orElse(ConnectionType.IN_MEMORY);
    }

}

