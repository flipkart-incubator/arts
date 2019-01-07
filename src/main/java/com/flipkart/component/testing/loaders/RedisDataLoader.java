package com.flipkart.component.testing.loaders;

import com.flipkart.component.testing.model.redis.RedisIndirectInput;
import com.flipkart.component.testing.shared.ObjectFactory;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

public class RedisDataLoader implements TestDataLoader<RedisIndirectInput> {

    @Override
    public void load(RedisIndirectInput indirectInput) {
        Jedis jedis = ObjectFactory.getRedisOperations(indirectInput).getJedis();
        indirectInput.getDBIndices().forEach(dbIndex -> {
            jedis.select(dbIndex);
            loadHashMaps(indirectInput.getHashMaps(dbIndex), jedis);
            loadKeyValues(indirectInput.getKeyValues(dbIndex), jedis);
            loadSet(indirectInput.getSets(dbIndex), jedis);
            loadSortedSet(indirectInput.getSortedSets(dbIndex), jedis);
        });
    }

    private void loadSet(Map<String, Set<String>> sets, Jedis jedis) {
        if(sets == null) return;
        sets.forEach((key,smembers) -> smembers.forEach((s1 -> jedis.sadd(key,s1))));
    }

    private void loadHashMaps(Map<String, Map<String, String>> redisHashMap, Jedis jedis) {
        if (redisHashMap == null) return;
        redisHashMap.forEach((redisKey, innerMap) -> innerMap.forEach((innerKey, value) -> jedis.hset(redisKey, innerKey, value)));
    }

    private void loadKeyValues(Map<String, String> keyValues, Jedis jedis) {
        if (keyValues == null) return;
        keyValues.forEach(jedis::set);
    }

    private void loadSortedSet(Map<String, Map<String, Double>> sortedSets, Jedis jedis) {
        if(sortedSets == null) return;
        sortedSets.forEach((set, scoreToMemberMap) -> scoreToMemberMap.forEach((member, score) -> jedis.zadd(set, score, member)));
    }
}
