package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.http.HttpObservation;
import com.flipkart.component.testing.model.Observation;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class SamplesTest {

    /**
     * DirectInput => Http
     * IndirectInput => Http
     * Observation => Http
     */
    @Test
    public void sample1() throws Exception {
        File file = new File(this.getClass().getClassLoader().getResource("sample1.json").getFile());
        TestSpecification testSpecification = new ObjectMapper().readValue(file, TestSpecification.class);
        List<Observation> observations = new HttpTestOrchestrator().run(testSpecification, () -> "http://localhost:7777");

        Assert.assertEquals(1, observations.size());
        Assert.assertTrue(observations.get(0) instanceof HttpObservation);

        HttpObservation httpObservation = (HttpObservation) observations.get(0);
        Object response = httpObservation.getResponse();
        Assert.assertEquals("{\"a\":\"b\"}", response);

    }


    /**
     * Having a bad indirect input should throw exception with correct message
     */
    @Test(expected = IllegalArgumentException.class)
    public void badIndirectInput() throws Exception {
        File file = new File(this.getClass().getClassLoader().getResource("badIndirectInput.json").getFile());
        TestSpecification testSpecification = new ObjectMapper().readValue(file, TestSpecification.class);
        List<Observation> observations = new HttpTestOrchestrator().run(testSpecification, () -> "http://localhost:7777");
    }

}
