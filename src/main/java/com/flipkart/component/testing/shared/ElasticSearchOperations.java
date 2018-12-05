package com.flipkart.component.testing.shared;

import org.elasticsearch.client.Client;

public interface ElasticSearchOperations {

    Client getClient();

    void startCluster();

    void stopCluster();

    void refresh();


}
