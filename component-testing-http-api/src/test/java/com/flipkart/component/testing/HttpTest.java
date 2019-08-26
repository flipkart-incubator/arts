package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.http.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class HttpTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Observation> observations;
    private static SpecificationRunner specificationRunner;

    @BeforeClass
    public static void initialize(){
        specificationRunner = new SpecificationRunner(() -> "http://localhost:7777");
    }

    @Test
    public void testWithDuplicateResource() throws Exception {
        observations = specificationRunner.runLite(objectMapper.readValue(new File("src/test/resources/specification.json"), TestSpecification.class));
        Assert.assertNotNull(observations.get(0));
        Map responseAsMap = ((HttpObservation)observations.get(0)).getResponseAsMap();
        Assert.assertEquals("b", responseAsMap.get("a"));
    }


    @Test
    public void testForMultipleInputs() throws Exception {
        observations = specificationRunner.runLite(objectMapper.readValue(new File("src/test/resources/testForMultipleInputs.json"), TestSpecification.class));
        Assert.assertNotNull(observations.get(0));
        Map responseAsMap = ((HttpObservation)observations.get(0)).getResponseAsMap();
        Assert.assertEquals("e", responseAsMap.get("d"));
    }
}
