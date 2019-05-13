package com.flipkart.component.testing;

import redis.clients.jedis.Jedis;

public interface RedisOperations {

    Jedis getJedis();

    void start();

    void stop();

    void flushAll();
}
