package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.shared.ElasticSearchTestConfig;
import com.flipkart.component.testing.shared.ObjectFactory;

class ElasticSearchLocalServer extends DependencyInitializer {

    private final ElasticSearchTestConfig elasticSearchTestConfig;

    ElasticSearchLocalServer(ElasticSearchTestConfig elasticSearchTestConfig) {
        this.elasticSearchTestConfig = elasticSearchTestConfig;
    }

    @Override
    public void initialize() {
        ObjectFactory.getESOperations(this.elasticSearchTestConfig).startCluster();
    }

    @Override
    public void shutDown() {
        ObjectFactory.getESOperations(this.elasticSearchTestConfig).stopCluster();
    }
}
