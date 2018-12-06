package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.http.HttpIndirectObservation;
import com.flipkart.component.testing.shared.ObjectFactory;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.List;

class HttpInteractionConsumer implements ObservationCollector<HttpIndirectObservation> {

    /**
     * extracts all the interactions happened with mock server
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public HttpIndirectObservation actualObservations(HttpIndirectObservation expectedObservation) {
        List<ServeEvent> allInteractions = ObjectFactory.getMockServerOperations().getAllInteractions();
        return new HttpIndirectObservation(allInteractions);
    }
}
