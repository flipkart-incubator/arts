package com.flipkart.component.testing.model.hazelcast;

import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.shared.HazelcastTestConfig;

import java.util.Map;

/**
 * @author siddharth.t
 */
public interface HazelcastIndirectInput extends IndirectInput, HazelcastTestConfig {

    default boolean isEmbeddedMode(){
        return this instanceof EmbeddedHZIndirectInput;
    }

    default boolean isServerMode(){
        return this instanceof ServerHZIndirectInput;
    }

    Map<String, HazelcastMap> getMaps();

}
