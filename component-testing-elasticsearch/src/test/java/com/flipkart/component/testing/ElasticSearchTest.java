package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchObservation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class ElasticSearchTest {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@Ignore
	public void test() throws Exception {

		TestSpecification testSpecification = objectMapper.readValue(new File("src/test/resources/es-test-data.json"), TestSpecification.class);
		List<Observation> observations = new HttpTestOrchestrator(() -> null).runLite(testSpecification);
		Assert.assertTrue(observations.size() == 1);
		Assert.assertTrue(observations.get(0) instanceof ElasticSearchObservation);
		ElasticSearchObservation actualObservation = (ElasticSearchObservation) observations.get(0);
		Assert.assertTrue(actualObservation.getDocumentsToFetch().get(0).getEsData().size()>0);
	}
}
