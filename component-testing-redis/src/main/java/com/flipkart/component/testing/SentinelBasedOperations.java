package com.flipkart.component.testing;

import com.flipkart.component.testing.model.redis.RedisTestConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.embedded.RedisCluster;
import redis.embedded.util.JedisUtil;

import java.util.Map;
import java.util.Set;

class SentinelBasedOperations implements RedisOperations {

    private final RedisCluster redisCluster;
    private final RedisTestConfig redisTestConfig;

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
            JedisSentinelPool pool = new JedisSentinelPool(redisTestConfig.getMasterName(), sentinelHosts);
            jedis = pool.getResource();
        }
        return jedis;
    }

    @Override
    public void start() {
        redisCluster.start();
    }

    @Override
    public void stop() {
        redisCluster.stop();
    }

    @Override
    public void flushAll() {
        this.getJedis().flushAll();
    }
}
