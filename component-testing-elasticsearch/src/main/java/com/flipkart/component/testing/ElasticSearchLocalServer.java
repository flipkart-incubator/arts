package com.flipkart.component.testing;


import com.flipkart.component.testing.model.elasticsearch.ElasticSearchIndirectInput;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchObservation;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchTestConfig;

class ElasticSearchLocalServer implements DependencyInitializer<ElasticSearchIndirectInput, ElasticSearchObservation, ElasticSearchTestConfig> {

    private ElasticSearchTestConfig elasticSearchTestConfig;


    @Override
    public void initialize(ElasticSearchTestConfig testConfig) throws Exception {
        this.elasticSearchTestConfig = testConfig;
        ESFactory.getESOperations(this.elasticSearchTestConfig).startCluster();
    }

    @Override
    public void shutDown() {
        ESFactory.getESOperations(this.elasticSearchTestConfig).stopCluster();
    }

    @Override
    public void clean() {
        ESFactory.getESOperations(this.elasticSearchTestConfig).deleteIndices();
    }

    @Override
    public Class<ElasticSearchIndirectInput> getIndirectInputClass() {
        return ElasticSearchIndirectInput.class;
    }

    @Override
    public Class<ElasticSearchObservation> getObservationClass() {
        return ElasticSearchObservation.class;
    }


}
