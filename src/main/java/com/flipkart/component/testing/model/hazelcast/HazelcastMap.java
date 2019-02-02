package com.flipkart.component.testing.model.hazelcast;

import lombok.Data;

import java.util.Map;

/**
 * @author siddharth.t
 */
@Data
public class HazelcastMap {

    private Map<Object, Object> mapData;

    private String keyClass;

    private String valueClass;
}
