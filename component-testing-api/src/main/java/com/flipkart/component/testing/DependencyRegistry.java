package com.flipkart.component.testing;

import com.flipkart.component.testing.model.TestSpecification;

/**
 * A tests specification composed of loaders and observations:
 * based on that corresponding services/local servers will be registered and initialized
 */
public interface DependencyRegistry extends DependencyInitializer {

    void registerDependencies(TestSpecification testSpecification);
}
