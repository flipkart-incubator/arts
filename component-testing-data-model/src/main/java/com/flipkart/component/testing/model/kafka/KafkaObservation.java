package com.flipkart.component.testing.model.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestConfig;
import lombok.Getter;

import java.util.List;

@JsonTypeName("kafkaObservation")
@Getter
public class KafkaObservation implements Observation, TestConfig {

    private final List<String> messages;

    private String topic;

    private String name;

    @JsonCreator
    public KafkaObservation(@JsonProperty("messages") List<String> messages, @JsonProperty("topic") String topic) {
        this.messages = messages;
        this.topic = topic;
    }

}
