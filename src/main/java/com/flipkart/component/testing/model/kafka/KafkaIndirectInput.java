package com.flipkart.component.testing.model.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;

import java.util.List;

@JsonTypeName("kafkaIndirectInput")
public class KafkaIndirectInput implements IndirectInput {

    private final List<Object> messages;

    private String topic;

//    private String serializerClass;

    private String name;

    @JsonCreator
    public KafkaIndirectInput(@JsonProperty("topic") String topic,
                              @JsonProperty("messages") List<Object> messages) {
        this.topic = topic;
        this.messages = messages;
    }

    public List<Object> getMessages() {
        return messages;
    }

    public String getTopic() {
        return topic;
    }

    /**
     * config for indirect input to whether load before or after SUT start
     *
     * @return
     */
    @Override
    public boolean isLoadAfterSUT() {
        return false;
    }
}
