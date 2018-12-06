package com.flipkart.component.testing.orchestrators;

import com.flipkart.component.testing.model.*;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.kafka.KafkaIndirectInput;
import com.flipkart.component.testing.model.kafka.KafkaObservation;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.mock;

public class KafkaTest {


    @Test
    public void testKafka() throws Exception {

        String topic = "abc";
        List<Object> messages = newArrayList("msg1", "msg2", "msg3");
        IndirectInput indirectInput = new KafkaIndirectInput(topic, null, messages);
        List<String> msgs = messages.stream().map(m -> (String) m).collect(Collectors.toList());
        KafkaObservation expectedObservation = new KafkaObservation(msgs, topic);

        TestSpecification testSpecification = new TestSpecification(null, newArrayList(indirectInput), newArrayList(expectedObservation));
        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification, () -> "");


        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof KafkaObservation);

        KafkaObservation kafkaObservation = (KafkaObservation) observations.get(0);

        //check
        Assert.assertTrue(kafkaObservation.getMessages().equals(msgs));
        Assert.assertTrue(kafkaObservation.getTopic().equals(topic));

    }
}
