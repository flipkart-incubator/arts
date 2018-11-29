package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.util.List;

@JsonTypeName("rmqObservation")
@Getter
public class RMQObservation implements Observation {

    private List<String> messages;
    private String queue;

    private String name;

    @JsonCreator
    public RMQObservation(@JsonProperty("messages") List<String> messages, @JsonProperty("queue") String queue) {
        this.messages = messages;
        this.queue = queue;
    }

}
