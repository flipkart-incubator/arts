package com.flipkart.component.testing.servers;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.flipkart.component.testing.internal.Utils;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.flipkart.component.testing.internal.Constants.HTTP_MOCK_SERVER_PORT;


/**
 * spawns a http mock server on port 7777
 */
class DefaultMockServer extends DependencyInitializer {

    private static DefaultMockServer instance;
    private WireMockServer wireMockServer;


    private DefaultMockServer() {
    }

    public static DefaultMockServer getInstance() {
        if (instance == null) {
            instance = new DefaultMockServer();

        }
        return instance;
    }

    public void initialize() {
        wireMockServer = new WireMockServer(options().port(HTTP_MOCK_SERVER_PORT));
        wireMockServer.start();

    }




    public void load(HttpIndirectInput indirectInput) {
        try {
            HttpResponse httpResponse = Utils.getPostResponse("http://localhost:7777/__admin/mappings", indirectInput.getSpecification(), ContentType.APPLICATION_JSON);
            if (httpResponse.getStatusLine().getStatusCode() != 201) {
                throw new IllegalArgumentException("Mock is not registered correctly.Check the json part of indirect Input" + httpResponse.getEntity());
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}