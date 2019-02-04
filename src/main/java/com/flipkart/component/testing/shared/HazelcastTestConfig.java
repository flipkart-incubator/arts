package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.model.hazelcast.EmbeddedHZIndirectInput;
import com.flipkart.component.testing.model.hazelcast.ServerHZIndirectInput;
import java.util.Map;

/**
 * @author siddharth.t
 */
public interface HazelcastTestConfig {

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
