package com.flipkart.component.testing.shared;

import java.util.Map;

/**
 * @author siddharth.t
 */
public interface HazelcastTestConfig {

    Map<String, String> getSerializerConfigMap();
    
    String getGroupName();

    String getPassword();

}
