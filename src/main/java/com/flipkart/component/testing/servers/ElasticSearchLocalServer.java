package com.flipkart.component.testing.servers;

import clojure.lang.Obj;
import com.flipkart.component.testing.Constants;
import com.flipkart.component.testing.shared.ElasticSearchTestConfig;
import com.flipkart.component.testing.shared.ObjectFactory;
import org.elasticsearch.client.Client;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

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
