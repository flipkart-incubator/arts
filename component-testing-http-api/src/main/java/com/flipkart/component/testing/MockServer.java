package com.flipkart.component.testing;

import com.flipkart.component.testing.model.TestConfig;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.http.HttpObservation;

/**
 * spawns a http mock server on port 7777
 */
class MockServer implements DependencyInitializer<HttpIndirectInput, HttpObservation, TestConfig> {

    private final MockServerOperations mockServerOperations;

    MockServer(){
        mockServerOperations = MockServerOperations.INSTANCE;
    }

    @Override
    public void initialize(TestConfig testConfig) throws Exception {
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

    @Override
    public Class<HttpIndirectInput> getIndirectInputClass() {
        return HttpIndirectInput.class;
    }

    @Override
    public Class<HttpObservation> getObservationClass() {
        return HttpObservation.class;
    }

}