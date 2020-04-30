package com.flipkart.component.testing;

import org.elasticsearch.client.RestHighLevelClient;

public interface ElasticSearchV2Operations {

    RestHighLevelClient getClient();

    void startCluster();

    void stopCluster();

    void refresh(String[] indices);

    void deleteIndex(String index);

    boolean isIndexPresent(String index);

    void createIndex(String index, String type, String mappingFileContent);

    void deleteIndices();
}
