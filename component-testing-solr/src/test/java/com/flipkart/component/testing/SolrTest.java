package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.solr.SolrObservation;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class SolrTest {


	@Test
	public void test() throws Exception {
       ObjectMapper objectMapper = new ObjectMapper();
        TestSpecification testSpecification = objectMapper
                .readValue(new File("src/test/resources/solrTestData.json"), TestSpecification.class);
        List<Observation> observations = new SpecificationRunner(() -> null).runLite(testSpecification);
        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof SolrObservation);
        SolrObservation actualObservation = (SolrObservation) observations.get(0);
	}
}
