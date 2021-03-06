package com.flipkart.component.testing;

import com.flipkart.component.testing.model.redis.RedisIndirectInput;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisDataLoader implements TestDataLoader<RedisIndirectInput> {

    @Override
    public void load(RedisIndirectInput indirectInput) {
        Jedis jedis = RedisFactory.getRedisOperations(indirectInput).getJedis();
        indirectInput.getDBIndices().forEach(dbIndex -> {
            jedis.select(dbIndex);
            loadHashMaps(indirectInput.getHashMaps(dbIndex), jedis);
            loadKeyValues(indirectInput.getKeyValues(dbIndex), jedis);
            loadSet(indirectInput.getSets(dbIndex), jedis);
            loadSortedSet(indirectInput.getSortedSets(dbIndex), jedis);
            loadLists(indirectInput.getLists(dbIndex), jedis);
        });
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<RedisIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(RedisIndirectInput.class);
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

    private void loadLists(Map<String, List<String>> lists, Jedis jedis) {
        if(lists == null) return;
        lists.forEach((key, list) -> jedis.rpush(key, list.toArray(new String[list.size()])));
    }

}
