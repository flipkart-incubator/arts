package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.internal.Constants;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.io.IOException;

class SingleHostBasedOperations implements RedisOperations {

    private RedisServer redisServer;

    SingleHostBasedOperations() {
        try {
            this.redisServer = new RedisServer(Constants.REDIS_SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Jedis getJedis() {
        return new Jedis();
    }

    @Override
    public void start() {
        this.redisServer.start();
    }

    @Override
    public void stop() {
        this.redisServer.stop();
    }
}
