package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.SUT;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.hazelcast.HazelcastObservation;
import com.hazelcast.core.Hazelcast;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * @author siddharth.t
 */
public class HazelcastEmbeddedModeTest {

    @Test
    public void test() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        SUT testSUT = new SUT() {
            @Override
            public String getUrl() {
                return "http://localhost:7777";
            }

            @Override
            public void start() {
                //as in embedded case application will initialise the hazelcast instance
                Hazelcast.newHazelcastInstance();
            }
        };
        TestSpecification testSpecification = objectMapper
                .readValue(new File("src/test/resources/embedded-hz-example.json"),TestSpecification.class);
        List<Observation> observations = new SpecificationRunner(testSUT).runLite(testSpecification);
        HazelcastObservation hazelcastObservation = (HazelcastObservation)observations.get(0);
        Assert.assertEquals(hazelcastObservation.getHazelcastDS().getMaps().get("testMap").getMapData().get("testKey"),
                "testValue");

    }
}