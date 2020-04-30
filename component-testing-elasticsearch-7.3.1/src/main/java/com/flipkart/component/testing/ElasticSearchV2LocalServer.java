package com.flipkart.component.testing;


import com.flipkart.component.testing.model.elasticsearch.v2.ElasticSearchV2Config;
import com.flipkart.component.testing.model.elasticsearch.v2.ElasticSearchV2IndirectInput;
import com.flipkart.component.testing.model.elasticsearch.v2.ElasticSearchV2Observation;

class ElasticSearchV2LocalServer implements DependencyInitializer<ElasticSearchV2IndirectInput,
        ElasticSearchV2Observation, ElasticSearchV2Config> {

    private ElasticSearchV2Config elasticSearchTestConfig;

    @Override
    public void initialize(ElasticSearchV2Config testConfig) {
        this.elasticSearchTestConfig = testConfig;
        ESV2Factory.getESOperations(this.elasticSearchTestConfig).startCluster();
    }

    @Override
    public void shutDown() {
        ESV2Factory.getESOperations(this.elasticSearchTestConfig).stopCluster();
    }

    @Override
    public void clean() {
        ESV2Factory.getESOperations(this.elasticSearchTestConfig).deleteIndices();
    }

    @Override
    public Class<ElasticSearchV2IndirectInput> getIndirectInputClass() {
        return ElasticSearchV2IndirectInput.class;
    }

    @Override
    public Class<ElasticSearchV2Observation> getObservationClass() {
        return ElasticSearchV2Observation.class;
    }


}
