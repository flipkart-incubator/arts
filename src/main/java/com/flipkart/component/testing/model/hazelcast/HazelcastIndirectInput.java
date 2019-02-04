package com.flipkart.component.testing.model.hazelcast;

import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.shared.HazelcastTestConfig;

import java.util.Map;

/**
 * @author siddharth.t
 */
public interface HazelcastIndirectInput extends IndirectInput, HazelcastTestConfig {
    Map<String, HazelcastMap> getMaps();
}
