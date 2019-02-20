package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.shared.ObjectFactory;
import com.flipkart.component.testing.shared.SolrTestConfig;

class SolrLocalServer implements DependencyInitializer {

    private final SolrTestConfig solrTestConfig;

   SolrLocalServer(SolrTestConfig solrTestConfig) {
        this.solrTestConfig = solrTestConfig;
    }

    @Override
    public void initialize() {
        ObjectFactory.getSolrOperations(this.solrTestConfig).startServer();
    }

    @Override
    public void shutDown() {
       ObjectFactory.getSolrOperations(this.solrTestConfig).stopServer();
    }

    @Override
    public void clean() {
       ObjectFactory.getSolrOperations(this.solrTestConfig).clean();
   }
}
