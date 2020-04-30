package com.flipkart.component.testing.model.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.TestConfig;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@JsonTypeName("kafkaIndirectInput")
public class KafkaIndirectInput implements IndirectInput, KafkaTestConfig {

    @Getter
    private final List<Object> messages;

    @Getter
    private String topic;

//    private String serializerClass;

    @Getter
    private KafkaAuthentication authentication;

    @JsonCreator
    public KafkaIndirectInput(@JsonProperty("topic") String topic,
                              @JsonProperty("messages") List<Object> messages,
                              @JsonProperty("authentication") KafkaAuthentication authentication ) {
        this.topic = topic;
        this.messages = messages;
        this.authentication = authentication;
    }

    public KafkaIndirectInput(String topic, List<Object> messages) {
       this(topic,messages,null);
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
