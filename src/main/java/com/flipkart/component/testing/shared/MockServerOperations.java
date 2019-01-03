package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.http.HttpIndirectObservation;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.List;

public interface MockServerOperations {

    List<ServeEvent> getAllInteractions();

    void load(HttpIndirectInput httpIndirectInput);

    void start();

    void shutDown();

    void clearAll();
}
