package com.flipkart.component.testing;

import com.flipkart.component.testing.model.solr.SolrIndirectInput;
import com.flipkart.component.testing.model.solr.SolrObservation;
import com.flipkart.component.testing.model.solr.SolrTestConfig;

class SolrLocalServer implements DependencyInitializer<SolrIndirectInput, SolrObservation, SolrTestConfig> {

    private  SolrTestConfig solrTestConfig;

    @Override
    public void initialize(SolrTestConfig testConfig) throws Exception {
       this.solrTestConfig = testConfig;
        SolrFactory.getSolrOperations(this.solrTestConfig).startServer();
    }

    @Override
    public void shutDown() {
       SolrFactory.getSolrOperations(this.solrTestConfig).stopServer();
    }

    @Override
    public void clean() {
       SolrFactory.getSolrOperations(this.solrTestConfig).clean();
   }

    @Override
    public Class<SolrIndirectInput> getIndirectInputClass() {
        return SolrIndirectInput.class;
    }

    @Override
    public Class<SolrObservation> getObservationClass() {
        return SolrObservation.class;
    }
}
