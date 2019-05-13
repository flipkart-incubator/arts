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

public class S3Test {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testS3() throws Exception {


        TestSpecification testSpecification = objectMapper.readValue(new File("src/test/resources/s3-example.json"),TestSpecification.class);
        List<Observation> observations = new SpecificationRunner(() -> null).runLite(testSpecification);

//        Assert.assertTrue(observations.size() == 1);
//        RMQObservation rmqObservation = (S) observations.get(0);
//        Assert.assertTrue(rmqObservation.getMessages().size() == 3);


    }

}
