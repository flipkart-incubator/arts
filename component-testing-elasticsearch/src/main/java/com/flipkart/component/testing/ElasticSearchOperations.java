package com.flipkart.component.testing;

import org.elasticsearch.client.Client;

public interface ElasticSearchOperations {

    Client getClient();

    void startCluster();

    void stopCluster();

    void refresh(String[] indices);

    void deleteIndex(String index);

    boolean isIndexPresent(String index);

    boolean isTypePresent(String index , String type);

    void createIndex(String index, String type);

    void deleteIndices();

    void executePutMapping(String index, String type, String mapping);

}
