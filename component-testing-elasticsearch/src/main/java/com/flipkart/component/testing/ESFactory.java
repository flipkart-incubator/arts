package com.flipkart.component.testing;

import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchTestConfig;

class ESFactory {

    public static final String ES_CLUSTER_PORT = "9210";
    public static final String ES_CLUSTER_VERSION = "1.5.2";
    public static final String ES_CLIENT_HOST = "127.0.0.1";
    public static final int ES_CLIENT_PORT =9300;
    public static final String ES_CLUSTER_NAME = "elasticsearch";

    public static ElasticSearchOperations getESOperations(ElasticSearchTestConfig elasticSearchTestConfig){
        if (elasticSearchTestConfig.getConnectionType() == ConnectionType.IN_MEMORY) {
            return EmbeddedESOperations.getInstance(elasticSearchTestConfig);
        } else {
            return RemoteElasticSearchOperations.getInstance(elasticSearchTestConfig);
        }
    }
}
