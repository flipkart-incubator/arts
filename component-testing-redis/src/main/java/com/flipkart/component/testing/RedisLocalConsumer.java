package com.flipkart.component.testing;

import com.flipkart.component.testing.model.redis.RedisObservation;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        Jedis jedis = RedisFactory.getRedisOperations(expectedObservation).getJedis();

        RedisObservation actualObservation = new RedisObservation(expectedObservation);

        expectedObservation.getDbIndices().forEach(dbIndex -> {
            Collector collector = new Collector(jedis, dbIndex).collect();
            actualObservation.addHashMap(dbIndex, collector.hashMaps);
            actualObservation.addKeyValues(dbIndex, collector.keyValues);
            actualObservation.addSet(dbIndex, collector.set);
            actualObservation.addSortedSet(dbIndex, collector.sortedSet);
            actualObservation.addLists(dbIndex, collector.lists);
        });

        return actualObservation;
    }

    @Override
    public Class<RedisObservation> getObservationClass() {
        return RedisObservation.class;
    }


    private class Collector {
        private final Jedis jedis;
        Map<String, Map<String, String>> hashMaps;
        Map<String, String> keyValues;
        Map<String, Set<String>> set;
        Map<String, Map<String,Double>> sortedSet;
        Map<String, List<String>> lists;


        Collector(Jedis jedis, int dbIndex) {
            this.jedis = jedis;
            this.jedis.select(dbIndex);
            this.hashMaps = new HashMap<>();
            this.keyValues = new HashMap<>();
            this.sortedSet = new HashMap<>();
            this.set = new HashMap<>();
            this.lists = new HashMap<>();
        }

        private void collect(String key) {
            String type = jedis.type(key);
            if ("string".equals(type)) {
                keyValues.put(key, jedis.get(key));
            } else if ("hash".equals(type)) {
                Map<String, String> hashMap = jedis.hgetAll(key);
                hashMaps.put(key, hashMap);
            } else if("set".equals(type)){
                Set<String> smembers = jedis.smembers(key);
                set.put(key, smembers);
            } else if ("zset".equals(type)){
                Set<Tuple> tuples = jedis.zrangeWithScores(key, 0, -1);
                sortedSet.putIfAbsent(key, new HashMap<>());
                tuples.forEach(tuple -> sortedSet.get(key).put(tuple.getElement(),tuple.getScore()));
            } else if("list".equals(type)){
                List<String> list = jedis.lrange(key, 0, -1);
                this.lists.put(key, list);
            }
        }

        Collector collect() {
            jedis.keys("*").forEach(this::collect);
            return this;
        }
    }
}