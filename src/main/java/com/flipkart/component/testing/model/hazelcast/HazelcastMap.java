package com.flipkart.component.testing.model.hazelcast;

import lombok.Data;

import java.util.Map;

/**
 * @author siddharth.t
 */
@Data
public class HazelcastMap {

    Map<Object, Object> mapData;

    String keyClass;

    String valueClass;
}
