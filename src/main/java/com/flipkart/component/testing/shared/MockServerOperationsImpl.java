package com.flipkart.component.testing.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.internal.Utils;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.http.HttpIndirectObservation;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.List;

import static com.flipkart.component.testing.internal.Constants.HTTP_MOCK_SERVER_PORT;
import static com.flipkart.component.testing.shared.ObjectFactory.OBJECT_MAPPER;
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
        String mappingJson = null;
        try {
            mappingJson = OBJECT_MAPPER.writeValueAsString(httpIndirectInput.getSpecification());
            wireMockServer.addStubMapping(StubMapping.buildFrom(mappingJson));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("unable to serialize", e);
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
