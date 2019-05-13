package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.DirectInput;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.http.*;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

public class HttpTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testWithPathMatching() throws Exception {
        TestSpecification testSpecification = objectMapper.readValue(new File("src/test/resources/specification.json"), TestSpecification.class);
        SpecificationRunner specificationRunner = new SpecificationRunner(() -> "http://localhost:7777");
        List<Observation> observations = specificationRunner.runLite(testSpecification);
        Assert.assertNotNull(observations.get(0));
        Map responseAsMap = ((HttpObservation)observations.get(0)).getResponseAsMap();
        Assert.assertEquals("b", responseAsMap.get("a"));
    }
}
