package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.model.RedisClusterType;

public interface RedisTestConfig {

    String getMasterName();

    RedisClusterType getClusterType();
}
