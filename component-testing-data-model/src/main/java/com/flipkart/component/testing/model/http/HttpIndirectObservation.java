package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
//import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("httpIndirectObservation")
@Getter
@ToString
@NoArgsConstructor
public class HttpIndirectObservation implements Observation {

//    private List<ServeEvent> allServeEvents = new ArrayList<>();
//
//    public HttpIndirectObservation(List<ServeEvent> allServeEvents) {
//        this.allServeEvents = allServeEvents;
//    }
}
