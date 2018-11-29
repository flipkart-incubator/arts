package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.shared.RedisTestConfig;
import com.flipkart.component.testing.shared.ObjectFactory;

/**
 * starts a Redis local server
 */

public class RedisLocalServer extends DependencyInitializer {

    private final RedisTestConfig redisTestConfig;

    RedisLocalServer(RedisTestConfig redisTestConfig) {
        this.redisTestConfig = redisTestConfig;
    }

    @Override
    public void initialize() {
        ObjectFactory.getRedisOperations(this.redisTestConfig).start();
    }

    @Override
    public void shutDown() {
        ObjectFactory.getRedisOperations(this.redisTestConfig).stop();
    }

}
