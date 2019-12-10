package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
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

    private List<HttpServeEvents> allServeEvents = new ArrayList<>();

    public HttpIndirectObservation(List<HttpServeEvents> allServeEvents) {
        this.allServeEvents = allServeEvents;
    }
    public List<HttpServeEvents> getAllServeEvents() {
        return this.allServeEvents;
    }
}
