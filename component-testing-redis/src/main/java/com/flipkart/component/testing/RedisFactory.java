package com.flipkart.component.testing;

import com.flipkart.component.testing.model.redis.RedisTestConfig;

public class RedisFactory {

    public static RedisOperations getRedisOperations(RedisTestConfig redisTestConfig) {
        if (redisTestConfig.isSentinel()) {
            return SentinelBasedOperations.getFromPool(redisTestConfig);
        } else {
            return SingleHostBasedOperations.getInstance();
        }
    }
}
