package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.rmq.RMQObservation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class RMQTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testRMQ() throws Exception {


        TestSpecification testSpecification = objectMapper.readValue(new File("src/test/resources/rmq-example.json"),TestSpecification.class);
        List<Observation> observations = new SpecificationRunner(() -> null).runLite(testSpecification);

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof RMQObservation);
        RMQObservation rmqObservation = (RMQObservation) observations.get(0);
        Assert.assertTrue(rmqObservation.getMessages().size() == 3);


    }

}
