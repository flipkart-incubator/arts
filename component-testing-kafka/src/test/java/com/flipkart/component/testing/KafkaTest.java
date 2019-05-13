package com.flipkart.component.testing;

import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.kafka.KafkaIndirectInput;
import com.flipkart.component.testing.model.kafka.KafkaObservation;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class KafkaTest {


    @Test
    public void testKafka() throws Exception {

        String topic = "abc";
        List<Object> messages = newArrayList("msg1", "msg2", "msg3");
        IndirectInput indirectInput = new KafkaIndirectInput(topic,  messages);
        List<String> msgs = messages.stream().map(m -> (String) m).collect(Collectors.toList());
        KafkaObservation expectedObservation = new KafkaObservation(msgs, topic);

        TestSpecification testSpecification = new TestSpecification(null, null, newArrayList(indirectInput), newArrayList(expectedObservation), null);
        List<Observation> observations =  new SpecificationRunner(()-> null).runLite(testSpecification);

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof KafkaObservation);

        KafkaObservation kafkaObservation = (KafkaObservation) observations.get(0);

        //check
        Assert.assertTrue(kafkaObservation.getMessages().equals(msgs));
        Assert.assertTrue(kafkaObservation.getTopic().equals(topic));

    }
}
