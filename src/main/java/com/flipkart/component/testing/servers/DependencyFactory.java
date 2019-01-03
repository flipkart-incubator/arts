package com.flipkart.component.testing.servers;

class DependencyFactory {

    private static DependencyInitializer defaultMockServer = new DefaultMockServer();

    static DependencyInitializer getMockServer() {
        return defaultMockServer;
    }
}
