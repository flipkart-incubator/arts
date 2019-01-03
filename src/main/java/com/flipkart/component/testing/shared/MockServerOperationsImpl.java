package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.internal.Utils;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.http.HttpIndirectObservation;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.List;

import static com.flipkart.component.testing.internal.Constants.HTTP_MOCK_SERVER_PORT;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class MockServerOperationsImpl implements MockServerOperations {

    private static MockServerOperations instance;
    private final WireMockServer wireMockServer;


    private MockServerOperationsImpl() {
        wireMockServer = new WireMockServer(options().extensions(new ResponseTemplateTransformer(false)).port(HTTP_MOCK_SERVER_PORT));
    }

    public static MockServerOperations getInstance() {
        if (instance == null) {
            instance = new MockServerOperationsImpl();
        }
        return instance;
    }

    @Override
    public List<ServeEvent> getAllInteractions() {
        return wireMockServer.getAllServeEvents();
    }

    @Override
    public void load(HttpIndirectInput httpIndirectInput) {
        try {
            HttpResponse httpResponse = Utils.getPostResponse("http://localhost:7777/__admin/mappings", httpIndirectInput.getSpecification(), ContentType.APPLICATION_JSON);
            if (httpResponse.getStatusLine().getStatusCode() != 201) {
                throw new IllegalArgumentException("Mock is not registered correctly.Check the json part of indirect Input" + httpResponse.getEntity());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        wireMockServer.start();
    }

    @Override
    public void shutDown() {
        wireMockServer.stop();
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        wireMockServer.resetAll();
    }
}
