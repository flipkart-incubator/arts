package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.hazelcast.HazelcastObservation;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author siddharth.t
 */
public class HazelcastServerModeTest {

    @Test
    public void test() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        TestSpecification testSpecification = objectMapper
                .readValue(new File("src/test/resources/server-hz-example.json"),TestSpecification.class);
        List<Observation> observations = new SpecificationRunner(() -> null).runLite(testSpecification);
        HazelcastObservation hazelcastObservation = (HazelcastObservation)observations.get(0);
        Assert.assertEquals(hazelcastObservation.getHazelcastDS().getMaps().get("testMap").getMapData().get("testKey"),
                "testValue");
    }
}