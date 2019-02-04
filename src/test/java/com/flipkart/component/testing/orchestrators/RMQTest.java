package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.*;
import com.flipkart.component.testing.model.rmq.RMQIndirectInput;
import com.flipkart.component.testing.model.rmq.RMQObservation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.flipkart.component.testing.shared.ObjectFactory.OBJECT_MAPPER;
import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;

public class RMQTest {

    @Test
    public void testRMQ() throws Exception {


        TestSpecification testSpecification = OBJECT_MAPPER.readValue(this.getClass().getClassLoader().getResourceAsStream("rmq-example.json"),TestSpecification.class);
        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).runLite(testSpecification);

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof RMQObservation);
        RMQObservation rmqObservation = (RMQObservation) observations.get(0);
        Assert.assertTrue(rmqObservation.getMessages().size() == 3);


    }

}
