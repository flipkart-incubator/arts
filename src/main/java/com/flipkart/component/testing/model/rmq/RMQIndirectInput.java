package com.flipkart.component.testing.model.rmq;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import lombok.Getter;

import java.util.List;

@JsonTypeName("rmqIndirectInput")
@Getter
public class RMQIndirectInput implements IndirectInput {

    private final List<Object> messages;

    private final String queue;

    private final String routingKey;

    @JsonCreator
    public RMQIndirectInput(@JsonProperty("messages") List<Object> messages,
                            @JsonProperty("queue") String queue,
                            @JsonProperty("routingKey") String routingKey) {
        this.queue = queue;
        this.messages = messages;
        this.routingKey = routingKey;
    }

}
