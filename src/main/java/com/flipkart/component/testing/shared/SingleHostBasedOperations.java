package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.internal.Constants;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.io.IOException;

class SingleHostBasedOperations implements RedisOperations {

    private static SingleHostBasedOperations instance;
    private RedisServer redisServer;
    private Jedis jedis;

    private SingleHostBasedOperations() {
        try {
            this.redisServer = new RedisServer(Constants.REDIS_SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static SingleHostBasedOperations getInstance(){
        if(instance == null) {
            instance = new SingleHostBasedOperations();
        }
        return instance;
    }

    @Override
    public Jedis getJedis() {
        if(jedis == null){
            jedis = new Jedis();
        }
        return jedis;
    }

    @Override
    public void start() {
        this.redisServer.start();
    }

    @Override
    public void stop() {
        this.redisServer.stop();
    }

    @Override
    public void flushAll() {
        this.getJedis().flushAll();
    }
}
