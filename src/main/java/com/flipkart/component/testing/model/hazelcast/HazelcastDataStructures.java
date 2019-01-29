package com.flipkart.component.testing.model.hazelcast;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.Map;

/**
 * @author siddharth.t
 */

@Data
public class HazelcastDataStructures {


    private Map<String, HazelcastMap> maps;
    //TODO: add more data structures supported by Hazelcast,for now only map is use case


    public Map<String, HazelcastMap> getMaps() {
        return maps;
    }
}
