package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.shared.RedisTestConfig;
import com.flipkart.component.testing.shared.ObjectFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * starts a Redis local server
 */

public class RedisLocalServer implements DependencyInitializer {

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

    @Override
    public void clean() {
        ObjectFactory.getRedisOperations(this.redisTestConfig).flushAll();
    }

}
