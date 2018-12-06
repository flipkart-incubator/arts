package com.flipkart.component.testing.servers;


import com.flipkart.component.testing.model.http.HttpIndirectObservation;
import com.flipkart.component.testing.shared.MockServerOperations;
import com.flipkart.component.testing.shared.ObjectFactory;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.flipkart.component.testing.internal.Utils;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.flipkart.component.testing.internal.Constants.HTTP_MOCK_SERVER_PORT;


/**
 * spawns a http mock server on port 7777
 */
class DefaultMockServer extends DependencyInitializer {

    private final MockServerOperations mockServerOperations;

    DefaultMockServer(){
        mockServerOperations = ObjectFactory.getMockServerOperations();
    }

    @Override
    public void initialize() {
        mockServerOperations.start();
    }

    @Override
    public void shutDown() {
        mockServerOperations.shutDown();
    }
}