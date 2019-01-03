package com.flipkart.component.testing.shared;

import org.elasticsearch.client.Client;

public interface ElasticSearchOperations {

    Client getClient();

    void startCluster();

    void stopCluster();

    void refresh(String[] indices);

    void deleteIndex(String index);

    boolean isIndexPresent(String index);

    void createIndex(String index, String type, String mappingFileContent);

    void deleteIndices();
}
