package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.model.TestSpecification;

/**
 * interface to register a test specification which will later be used to initialize the dependencies
 */
public interface DependencyRegistry extends DependencyInitializer {


    DependencyRegistry INSTANCE = new DependencyRegistryImpl();

    /**
     * registers the dependencies for test specification.
     * @param testSpecification
     */
    void registerDependencies(TestSpecification testSpecification);

    /**
     * an utility method to register multiple dependencies
     * @param testSpecifications
     */
    default void registerDependencies(Iterable<TestSpecification> testSpecifications){
        testSpecifications.forEach(this::registerDependencies);
    }

    default void registerAndInitialize(TestSpecification testSpecification) throws Exception {
        registerDependencies(testSpecification);
        initialize();
    }

    default void registerAndInitialize(Iterable<TestSpecification> testSpecifications) throws Exception {
        registerDependencies(testSpecifications);
        initialize();
    }
}
