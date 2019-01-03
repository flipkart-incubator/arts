package com.flipkart.component.testing.servers;


interface DependencyInitializer {

    void initialize() throws Exception;

    void shutDown();

    void clean();

}
