package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

@JsonTypeName("kafkaIndirectInput")
public class KafkaIndirectInput implements IndirectInput {

    private final List<Object> messages;

    private String topic;

    private String serializerClass;

    private String name;

    @JsonCreator
    public KafkaIndirectInput(@JsonProperty("topic") String topic,
                              @JsonProperty("serializerClass") String serializerClass,
                              @JsonProperty("messages") List<Object> messages) {
        this.topic = topic;
        this.serializerClass = serializerClass;
        this.messages = messages;
    }

    public List<Object> getMessages() {
        return messages;
    }

    public String getTopic() {
        return topic;
    }

    public String getSerializerClass() {
        if (serializerClass == null) {
            serializerClass = "kafka.serializer.StringEncoder";
        }
        return serializerClass;
    }
}
