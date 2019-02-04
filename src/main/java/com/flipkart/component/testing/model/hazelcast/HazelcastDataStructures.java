package com.flipkart.component.testing.model.hazelcast;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author siddharth.t
 */
@Getter
@AllArgsConstructor
public class HazelcastDataStructures {


    private Map<String, HazelcastMap> maps;
    //TODO: add more data structures supported by Hazelcast,for now only map is use case


    public Map<String, HazelcastMap> getMaps() {
        return maps;
    }
}
