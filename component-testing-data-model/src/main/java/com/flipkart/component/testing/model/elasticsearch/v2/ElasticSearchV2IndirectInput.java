package com.flipkart.component.testing.model.elasticsearch.v2;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.elasticsearch.DocumentsOfIndexAndType;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchTestConfig;
import com.flipkart.component.testing.model.internal.Constants;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

/**
 * Elastic search Indirect Input
 */
@JsonTypeName("elasticSearchV2IndirectInput")
@Getter
public class ElasticSearchV2IndirectInput extends ElasticSearchV2Config implements IndirectInput {

    private final List<DocumentsOfIndexAndType> documentsOfIndexAndType;

    @JsonCreator
    public ElasticSearchV2IndirectInput(@JsonProperty("connectionInfo") ConnectionInfo connectionInfo,
                                        @JsonProperty("documentsOfIndexAndType") List<DocumentsOfIndexAndType> documentsOfIndexAndType) {
        super(connectionInfo);
        this.documentsOfIndexAndType = documentsOfIndexAndType;
        if(connectionInfo!=null && (connectionInfo.getConnectionType()!=ConnectionType.IN_MEMORY)) {
            documentsOfIndexAndType.forEach(document-> {
                if(!document.getIndex().contains("regression_"))
                    throw new IllegalArgumentException("Index name should have prefix 'regression_' for REMOTE connections");
            });
        }
    }

    @Override
    public String getClusterName() {
        return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getClusterName).orElse(Constants.ES_CLUSTER_NAME);
    }

    @Override
    public String getHost() {
        return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getHost).orElse(Constants.ES_CLIENT_HOST);
    }

    @Override
    public ConnectionType getConnectionType() {
        return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getConnectionType).orElse(ConnectionType.IN_MEMORY);
    }

    /**
     * config for indirect input to whether load before or after SUT start
     *
     * @return
     */
    @Override
    public boolean  isLoadAfterSUT() {
        return false;
    }
}

