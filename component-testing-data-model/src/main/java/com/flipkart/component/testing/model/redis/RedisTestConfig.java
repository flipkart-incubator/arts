package com.flipkart.component.testing.model.redis;

import com.flipkart.component.testing.model.TestConfig;

public interface RedisTestConfig extends TestConfig{

    String getMasterName();

    boolean isSentinel();
}
