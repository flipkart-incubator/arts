package com.flipkart.component.testing;


import com.flipkart.component.testing.model.http.HttpIndirectObservation;
import com.flipkart.component.testing.model.http.HttpServeEvents;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.ArrayList;
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

       List<ServeEvent> allInteractions = MockServerOperations.INSTANCE.getAllInteractions();
       List<HttpServeEvents> httpServeEvents = new ArrayList<>();
       for (ServeEvent serveEvent : allInteractions){
           httpServeEvents.add(new HttpServeEvents(serveEvent.getId(),
                   serveEvent.getRequest(),
                   serveEvent.getStubMapping(),
                   serveEvent.getResponseDefinition(),
                   serveEvent.getResponse(),
                   serveEvent.getTiming()));
       }
        return new HttpIndirectObservation(httpServeEvents);
    }

    @Override
    public Class<HttpIndirectObservation> getObservationClass() {
        return HttpIndirectObservation.class;
    }

}
