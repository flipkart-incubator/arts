package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.solr.SolrObservation;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

public class SolrTest {


	@Test
	public void test() throws Exception {
       ObjectMapper objectMapper = new ObjectMapper();
        TestSpecification testSpecification = objectMapper
                .readValue(this.getClass().getClassLoader().getResourceAsStream("solrTestData.json"), TestSpecification.class);
		List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification);
        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof SolrObservation);
        SolrObservation actualObservation = (SolrObservation) observations.get(0);
	}
}
