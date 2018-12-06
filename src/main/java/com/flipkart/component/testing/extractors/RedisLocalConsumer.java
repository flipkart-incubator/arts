package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.redis.RedisHashMap;
import com.flipkart.component.testing.model.redis.RedisObservation;
import com.flipkart.component.testing.shared.ObjectFactory;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Reads from the local Redis Consumer
 */
class RedisLocalConsumer implements ObservationCollector<RedisObservation> {

    /**
     * extracts the actual Observation using attributes from expected observation
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public RedisObservation actualObservations(RedisObservation expectedObservation) {
        Jedis jedis = ObjectFactory.getRedisOperations(expectedObservation).getJedis();
        List<RedisHashMap> redisHashMaps = loadHashMaps(expectedObservation.getHashMaps(), jedis);
        Map<String,String> keyValues = loadKeyValues(expectedObservation.getKeyValues(), jedis);
        return new RedisObservation(expectedObservation,redisHashMaps, keyValues);
    }

    private Map<String, String> loadKeyValues(Map<String, String> keyValues, Jedis jedis) {
        if(keyValues == null) return null;
        return jedis.keys("*").stream()
                .filter(key -> jedis.type(key).equals("string"))
                .collect(Collectors.toMap(k -> k, jedis::get));
    }

    private List<RedisHashMap> loadHashMaps(List<RedisHashMap> hashMaps, Jedis jedis) {
        if(hashMaps == null) return null;
        return jedis.keys("*").stream()
                .filter(key -> jedis.type(key).equals("hash"))
                .map(key -> new RedisHashMap(key, jedis.hgetAll(key)))
                .collect(Collectors.toList());
    }
}