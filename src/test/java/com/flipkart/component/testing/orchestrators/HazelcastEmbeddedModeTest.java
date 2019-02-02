package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.hazelcast.HazelcastObservation;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author siddharth.t
 */
public class HazelcastEmbeddedModeTest {

    @Test
    public void test() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TestSpecification testSpecification = objectMapper
                .readValue(this.getClass().getClassLoader()
                        .getResourceAsStream("hazelcast-example.json"),TestSpecification.class);
        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification);
        HazelcastObservation hazelcastObservation = (HazelcastObservation)observations.get(0);
        Assert.assertEquals(hazelcastObservation.getHazelcastDS().getMaps().get("testMap").getMapData().get("testKey"),
                "testValue");

    }
}