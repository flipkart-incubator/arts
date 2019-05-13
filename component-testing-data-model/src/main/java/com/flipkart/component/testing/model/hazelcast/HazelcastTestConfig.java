package com.flipkart.component.testing.model.hazelcast;

import com.flipkart.component.testing.model.TestConfig;

import java.util.Map;

/**
 * @author siddharth.t
 */
public interface HazelcastTestConfig extends TestConfig{

    Map<String, String> getSerializerConfigMap();
    
    String getGroup();

    String getPassword();

    String getUser();

    default boolean isEmbeddedMode(){
        return this instanceof EmbeddedHZIndirectInput;
    }

    default boolean isServerMode(){
        return this instanceof ServerHZIndirectInput;
    }

}
