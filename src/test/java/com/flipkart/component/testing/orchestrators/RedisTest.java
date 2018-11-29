package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.TestData;
import com.google.common.collect.Sets;
import com.flipkart.component.testing.HttpTestRunner;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.RedisObservation;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.List;

import static org.mockito.Mockito.mock;

public class RedisTest {

    @Test
    public void test() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        TestData testData = objectMapper.readValue(this.getClass().getClassLoader().getResourceAsStream("redis-example.json"),TestData.class);
        RedisObservation expectedObservation = (RedisObservation) testData.getObservations().get(0);

        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testData, () -> "");

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof RedisObservation);

        RedisObservation actualObservation = (RedisObservation) observations.get(0);
        Assert.assertEquals(expectedObservation.getHashMaps(), actualObservation.getHashMaps());
        Assert.assertEquals(expectedObservation.getKeyValues(),actualObservation.getKeyValues());
    }

}


