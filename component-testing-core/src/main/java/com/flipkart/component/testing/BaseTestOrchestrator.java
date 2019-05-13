package com.flipkart.component.testing;

/**
 * Common code for all test orchestrator
 */
abstract class BaseTestOrchestrator {

    /**
     * helps in loading the data
     */
    protected final TestDataLoader testDataLoader;

    /**
     * helps in fetching the data from the data stores
     */
    protected final ObservationCollector observationCollector;


    /**
     * helps in registering the dependencies
     */
    protected final DependencyRegistry dependencyRegistry;


    BaseTestOrchestrator() {
        this.testDataLoader = CompositeTestDataLoader.getInstance();
        this.observationCollector = CompositeObservationCollector.getInstance();
        this.dependencyRegistry = CompositeDependencyRegistry.getInstance();
    }

}
