package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.io.File;


public class AerospikeTest {


    @Test
    public void aerospikePutGetTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestSpecification testSpecification = mapper
                .readValue(new File("src/test/resources/aerospikeTestData.json"), TestSpecification.class);
        List<Observation> observations = new HttpTestOrchestrator(() -> null).run(testSpecification);
        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof AerospikeObservation);
        AerospikeObservation actualObservation = (AerospikeObservation) observations.get(0);
        Assert.assertEquals(testSpecification.getObservations().get(0), actualObservation);
    }

}
