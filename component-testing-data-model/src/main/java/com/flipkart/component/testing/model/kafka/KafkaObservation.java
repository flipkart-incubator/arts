package com.flipkart.component.testing.model.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestConfig;
import lombok.Getter;

import java.util.List;

@JsonTypeName("kafkaObservation")
public class KafkaObservation implements Observation, KafkaTestConfig {


    @Getter
    private final List<String> messages;
    @Getter
    private String topic;
    @Getter
    private KafkaAuthentication authentication;

    @JsonCreator
    public KafkaObservation(@JsonProperty("messages") List<String> messages,
                            @JsonProperty("topic") String topic,
                            @JsonProperty("authentication") KafkaAuthentication authentication ) {
        this.messages = messages;
        this.topic = topic;
        this.authentication = authentication;
    }

    public KafkaObservation( List<String> messages, String topic) {
       this(messages,topic,null);
    }

}
