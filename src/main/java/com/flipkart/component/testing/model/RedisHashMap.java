package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class RedisHashMap {

    private String key;

    private Map<String, String> data;

    @JsonCreator
    public RedisHashMap(@JsonProperty("key") String key,
                        @JsonProperty("data") Map<String, String> data) {

        this.key = key;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public Map<String, String> getData() {
        return data;
    }
}