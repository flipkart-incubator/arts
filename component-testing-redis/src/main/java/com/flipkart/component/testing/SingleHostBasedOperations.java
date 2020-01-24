package com.flipkart.component.testing;

import redis.clients.jedis.Jedis;
import redis.embedded.RedisServer;

import java.io.IOException;

class SingleHostBasedOperations implements RedisOperations {

    private static SingleHostBasedOperations instance;
    private RedisServer redisServer;
    private Jedis jedis;
    private static final int REDIS_SERVER_PORT = 6379;
    private static final String REDIS_SERVER_HOST = "127.0.0.1";
    private static final int JEDIS_CONNECTION_TIMEOUT = 100000;
    private static boolean serverRunning = false;

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
            jedis = new Jedis(REDIS_SERVER_HOST,REDIS_SERVER_PORT,JEDIS_CONNECTION_TIMEOUT);
        }
        return jedis;
    }

    @Override
    public void start() {
        //process is not getting killed hence
        if(!serverRunning){
        this.stopServerBeforeStart();
        this.redisServer.start();
        serverRunning = true;
        }
    }

    private void stopServerBeforeStart(){
        try {
            this.redisServer.stop();
        }catch (Exception e){
            throw new RuntimeException("Error stopping redis server : "+e);
        }
        try {
            Runtime.getRuntime().exec("pkill redis");
            Thread.sleep(3000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public void flushAll() {
        this.getJedis().flushAll();
    }
}
