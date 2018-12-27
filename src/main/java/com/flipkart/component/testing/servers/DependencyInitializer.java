package com.flipkart.component.testing.servers;


import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.TestSpecification;

public abstract class DependencyInitializer {

    public abstract void initialize() throws Exception;

    public static DependencyInitializer getInstance(TestSpecification testSpecification) {
        return new DependencyInitializerImpl(testSpecification);
    }

    public static DependencyInitializer getInstance(Iterable<TestSpecification> testSpecifications){
        return new DependencyInitializerImpl(testSpecifications);
    }

    public abstract void shutDown();

}
