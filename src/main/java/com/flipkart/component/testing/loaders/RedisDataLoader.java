package com.flipkart.component.testing.loaders;

import com.flipkart.component.testing.model.redis.RedisHashMap;
import com.flipkart.component.testing.model.redis.RedisIndirectInput;
import com.flipkart.component.testing.shared.ObjectFactory;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;

public class RedisDataLoader implements TestDataLoader<RedisIndirectInput> {

    @Override
    public void load(RedisIndirectInput indirectInput) {
        Jedis jedis = ObjectFactory.getRedisOperations(indirectInput).getJedis();
        loadHashMaps(indirectInput.getHashMaps(), jedis);
        loadKeyValues(indirectInput.getKeyValues(), jedis);


    }

    private void loadHashMaps(List<RedisHashMap> hashMaps, Jedis jedis) {

        if (hashMaps == null) return;

        for (RedisHashMap hashMap : hashMaps) {
            for (Map.Entry<String, String> kv : hashMap.getData().entrySet()) {
                jedis.hset(hashMap.getKey(), kv.getKey(), kv.getValue());
            }
        }

    }

    private void loadKeyValues(Map<String, String> keyValues, Jedis jedis) {
        if (keyValues == null) return;

        for (Map.Entry<String, String> kv : keyValues.entrySet()) {
            jedis.set(kv.getKey(), kv.getValue());
        }
    }
}
