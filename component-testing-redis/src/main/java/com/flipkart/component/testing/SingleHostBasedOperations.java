package com.flipkart.component.testing;

import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.io.IOException;

class SingleHostBasedOperations implements RedisOperations {

    private static SingleHostBasedOperations instance;
    private RedisServer redisServer;
    private Jedis jedis;
    public static final int REDIS_SERVER_PORT = 6379;

    private SingleHostBasedOperations() {
        try {
            this.redisServer = new RedisServer(REDIS_SERVER_PORT);
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
        //process is not getting killed hence
        this.stop();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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