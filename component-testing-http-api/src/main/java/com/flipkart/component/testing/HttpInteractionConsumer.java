package com.flipkart.component.testing;


import com.flipkart.component.testing.model.http.HttpIndirectObservation;
//import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

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
//        List<ServeEvent> allInteractions = MockServerOperations.INSTANCE.getAllInteractions();
//        return new HttpIndirectObservation(allInteractions);
        return null;
    }

    @Override
    public Class<HttpIndirectObservation> getObservationClass() {
        return HttpIndirectObservation.class;
    }

}
