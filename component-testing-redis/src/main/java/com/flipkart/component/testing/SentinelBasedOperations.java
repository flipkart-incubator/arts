package com.flipkart.component.testing;

import com.flipkart.component.testing.model.redis.RedisTestConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class SentinelBasedOperations implements RedisOperations {

    private final RedisCluster redisCluster;
    private final RedisTestConfig redisTestConfig;
    private static boolean serverRunning = false;

    private static Map<String, SentinelBasedOperations> pool= Maps.newHashMap();
    private Jedis jedis;

    public static SentinelBasedOperations getFromPool(RedisTestConfig redisTestConfig){
        if(pool.containsKey(redisTestConfig.getMasterName())){
            return pool.get(redisTestConfig.getMasterName());
        }

        SentinelBasedOperations sbo = new SentinelBasedOperations(redisTestConfig);
        pool.put(redisTestConfig.getMasterName(), sbo);
        return sbo;
    }

    private SentinelBasedOperations(RedisTestConfig redisTestConfig) {
        this.redisTestConfig = redisTestConfig;
        redisCluster = RedisCluster.builder().sentinelPorts(Lists.newArrayList(26379)).sentinelCount(1).quorumSize(1)
                .replicationGroup(redisTestConfig.getMasterName(), 1)
                .build();
    }

    @Override
    public Jedis getJedis() {
        if(jedis == null){
            Set<String> sentinelHosts = JedisUtil.sentinelHosts(redisCluster);
            setLocalInetAddress();
            JedisSentinelPool pool = new JedisSentinelPool(redisTestConfig.getMasterName(), sentinelHosts);
            jedis = pool.getResource();
        }
        return jedis;
    }

    private void setLocalInetAddress() {
        try {
            Field host = (HostAndPort.class.getDeclaredField("LOCALHOST_STR"));
            host.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField( "modifiers" );
            modifiersField.setAccessible( true );
            modifiersField.setInt( host, host.getModifiers() & ~Modifier.FINAL );
            host.set(null,"127.0.0.1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start() {
        //redisCluster.start();
        if(!serverRunning){
            this.stopServerBeforeStart();
            this.redisCluster.start();
            serverRunning = true;
        }
    }

    @Override
    public void stop() {
        // redisCluster.stop();
    }

    private void stopServerBeforeStart(){
        try {
            this.redisCluster.stop();
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
    public void flushAll() {
        this.getJedis().flushAll();
    }
}
