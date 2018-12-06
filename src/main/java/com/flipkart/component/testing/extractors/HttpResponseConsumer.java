package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.http.HttpObservation;

import static com.flipkart.component.testing.internal.TestContext.httpResponseMap;

class HttpResponseConsumer implements ObservationCollector<HttpObservation> {

    /**
     * prepares the httpObservation from the testContext
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public HttpObservation actualObservations(HttpObservation expectedObservation) {
        return new HttpObservation((Integer) httpResponseMap.get("responseStatus"), httpResponseMap.get("response"), httpResponseMap.get("responseHeaders"));
    }
}
