package com.flipkart.component.testing;

import com.flipkart.component.testing.model.redis.RedisIndirectInput;
import com.flipkart.component.testing.model.redis.RedisObservation;
import com.flipkart.component.testing.model.redis.RedisTestConfig;


/**
 * starts a Redis local server
 */

public class RedisLocalServer implements DependencyInitializer<RedisIndirectInput, RedisObservation, RedisTestConfig> {

    private RedisTestConfig redisTestConfig;

    @Override
    public void initialize(RedisTestConfig redisTestConfig) throws Exception {
        this.redisTestConfig = redisTestConfig;
        RedisOperations redisOperations = RedisFactory.getRedisOperations(this.redisTestConfig);
        redisOperations.start();
    }

    @Override
    public void shutDown() {
       RedisFactory.getRedisOperations(this.redisTestConfig).stop();
    }

    @Override
    public void clean() {
        RedisFactory.getRedisOperations(this.redisTestConfig).flushAll();
    }

    @Override
    public Class<RedisIndirectInput> getIndirectInputClass() {
        return RedisIndirectInput.class;
    }

    @Override
    public Class<RedisObservation> getObservationClass() {
        return RedisObservation.class;
    }


}
