package com.flipkart.component.testing.servers;


import com.flipkart.component.testing.shared.MockServerOperations;
import com.flipkart.component.testing.shared.ObjectFactory;


/**
 * spawns a http mock server on port 7777
 */
class DefaultMockServer implements DependencyInitializer {

    private final MockServerOperations mockServerOperations;

    DefaultMockServer(){
        mockServerOperations = ObjectFactory.getMockServerOperations();
    }

    @Override
    public void initialize() {
        mockServerOperations.start();
    }

    @Override
    public void shutDown() {
        mockServerOperations.shutDown();
    }

    @Override
    public void clean() {
        mockServerOperations.clearAll();
    }
}