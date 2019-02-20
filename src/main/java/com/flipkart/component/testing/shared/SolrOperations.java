package com.flipkart.component.testing.shared;


import com.flipkart.component.testing.model.ConnectionType;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;

public interface SolrOperations {
    void startServer();
    void stopServer();
    void createCore();
    void commit();
    void clean();
    void addSolrDocument(SolrInputDocument solrDocument);
    QueryResponse getQueryResponse(String queryFilePath);
}
