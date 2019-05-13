package com.flipkart.component.testing;


import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestConfig;

/**
 * initialize the dependeny
 * @param <INDIRECT_INPUT>
 * @param <OBSERVATION>
 */
interface DependencyInitializer<INDIRECT_INPUT extends IndirectInput, OBSERVATION extends Observation, TEST_CONFIG extends TestConfig> {

    void initialize(TEST_CONFIG testConfig) throws Exception;

    void shutDown();

    void clean();

    Class<INDIRECT_INPUT> getIndirectInputClass();

    Class<OBSERVATION> getObservationClass();


}
