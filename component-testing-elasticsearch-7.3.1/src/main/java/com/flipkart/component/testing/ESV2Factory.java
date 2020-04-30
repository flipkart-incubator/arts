package com.flipkart.component.testing;


import com.flipkart.component.testing.model.elasticsearch.v2.ElasticSearchV2Config;

class ESV2Factory {

    static final int ES_CLIENT_PORT = 9200;

    static ElasticSearchV2Operations getESOperations(ElasticSearchV2Config elasticSearchTestConfig){
        return RemoteElasticSearchV2Operations.getInstance(elasticSearchTestConfig);
    }
}
