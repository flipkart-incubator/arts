package com.flipkart.component.testing;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.solr.SolrTestConfig;

public class SolrFactory {

    public static SolrOperations getSolrOperations(SolrTestConfig solrTestConfig) {
        if (solrTestConfig.getConnectionType() == ConnectionType.IN_MEMORY)
            return EmbeddedSolrOperations.getEmbeddedSolrInstance(solrTestConfig);
        else
            return RemoteSolrOperations.getRemoteSolrInstance(solrTestConfig);
    }

}
