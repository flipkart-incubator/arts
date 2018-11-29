package com.flipkart.component.testing.orchestrators;

import com.flipkart.component.testing.HttpTestRunner;
import com.flipkart.component.testing.model.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;

public class RMQTest {


    @Test
    @Ignore //TODO:PAVAN revisit
    public void testRMQ() throws Exception {

        String queue = "abc";
        List<Object> messages = newArrayList("msg1", "msg2", "msg3");
        IndirectInput indirectInput = new RMQIndirectInput(messages, queue, "");

        List<String> msgs = messages.stream().map(m -> (String) m).collect(Collectors.toList());
        RMQObservation expectedObservation = new RMQObservation(msgs, queue);

        TestData testData = new TestData(null, newArrayList(indirectInput), newArrayList(expectedObservation));
        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testData, () -> "");

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof RMQObservation);

        RMQObservation rmqObservation = (RMQObservation) observations.get(0);

        Assert.assertTrue(rmqObservation.getMessages().equals(msgs));
        Assert.assertTrue(rmqObservation.getQueue().equals(queue));

    }

}
