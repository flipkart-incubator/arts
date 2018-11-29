package com.flipkart.component.testing.servers;


import com.flipkart.component.testing.model.HttpIndirectInput;
import com.flipkart.component.testing.model.TestData;

public abstract class DependencyInitializer {

    public abstract void initialize() throws Exception;

    public static DependencyInitializer getInstance(TestData testData) {
        return new DependencyInitializerImpl(testData);
    }

    public static void loadIndirectInput(HttpIndirectInput httpIndirectInput) {
        DefaultMockServer.getInstance().load(httpIndirectInput);
    }

    public abstract void shutDown();

}
