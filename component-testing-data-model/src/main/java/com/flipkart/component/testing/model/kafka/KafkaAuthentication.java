package com.flipkart.component.testing.model.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KafkaAuthentication {

    private String username;
    private String password;

    @JsonCreator
    public KafkaAuthentication(@JsonProperty("username")String username,
                               @JsonProperty("password")String password){
        this.username = username;
        this.password = password;
    }
}