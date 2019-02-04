package com.flipkart.component.testing.model.hazelcast;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @author siddharth.t
 */
@Getter
@AllArgsConstructor
public class HazelcastMap {

    private final Map<?, ?> mapData;

    private final String keyClass;

    private final String valueClass;
}
