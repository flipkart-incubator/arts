package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.redis.RedisObservation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RedisTest {

    @Test
    @Ignore
    public void redisSingleHostTest() throws Exception {


        TestSpecification testSpecification = new ObjectMapper().readValue(new File("src/test/resources/redis-singlehost.json"),TestSpecification.class);
        RedisObservation expectedObservation = (RedisObservation) testSpecification.getObservations().get(0);

        List<Observation> observations = new HttpTestOrchestrator(() -> null).runLite(testSpecification);

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof RedisObservation);

        RedisObservation actualObservation = (RedisObservation) observations.get(0);
        assertEquals(2, actualObservation.getDbToDSMap().size());
        assertEquals(2, actualObservation.getDbToDSMap().get(7).getHashMap().size());
        assertEquals(2, actualObservation.getDbToDSMap().get(7).getKeyValues().size());
        assertEquals(1, actualObservation.getDbToDSMap().get(7).getSortedSets().size());
        assertEquals(1, actualObservation.getDbToDSMap().get(7).getLists().size());
    }


    @Test
    @Ignore
    public void redisSentinelTest() throws Exception {


        TestSpecification testSpecification = new ObjectMapper().readValue(new File("src/test/resources/redis-sentinel.json"), TestSpecification.class);
        RedisObservation expectedObservation = (RedisObservation) testSpecification.getObservations().get(0);

        List<Observation> observations = new HttpTestOrchestrator(() -> null).runLite(testSpecification);

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof RedisObservation);

        RedisObservation actualObservation = (RedisObservation) observations.get(0);
        assertEquals(1, actualObservation.getDbToDSMap().size());

    }

}


