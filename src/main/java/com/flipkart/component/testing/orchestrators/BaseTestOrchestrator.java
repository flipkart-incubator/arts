package com.flipkart.component.testing.orchestrators;

import com.flipkart.component.testing.loaders.TestDataLoader;
import com.flipkart.component.testing.extractors.ObservationCollector;

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


    BaseTestOrchestrator() {
        this.testDataLoader = TestDataLoader.INSTANCE;
        this.observationCollector = ObservationCollector.INSTANCE;
    }

}
