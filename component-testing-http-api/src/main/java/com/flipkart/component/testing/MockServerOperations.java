package com.flipkart.component.testing;

import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.List;

interface MockServerOperations {

    MockServerOperations INSTANCE = MockServerOperationsImpl.getInstance();

    List<ServeEvent> getAllInteractions();

    void load(HttpIndirectInput httpIndirectInput);

    void start();

    void shutDown();

    void clearAll();
}
