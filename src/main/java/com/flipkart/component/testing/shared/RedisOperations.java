package com.flipkart.component.testing.shared;

import redis.clients.jedis.Jedis;

public interface RedisOperations {

    Jedis getJedis();

    void start();

    void stop();

    void flushAll();
}
