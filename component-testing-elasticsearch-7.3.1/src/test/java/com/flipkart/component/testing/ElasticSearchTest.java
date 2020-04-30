package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchObservation;
import com.flipkart.component.testing.model.elasticsearch.v2.ElasticSearchV2Observation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class ElasticSearchTest {

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Please make sure that there are no indices with the prefix 'regression_' in the remote cluster. This test case
	 * is built to delete these indices at the end of the test case.
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void test() throws Exception {

		TestSpecification testSpecification = objectMapper.readValue(new File("src/test/resources/es-test-data.json"), TestSpecification.class);
		List<Observation> observations = new HttpTestOrchestrator(() -> null).runLite(testSpecification);
		Assert.assertEquals(1, observations.size());
		Assert.assertTrue(observations.get(0) instanceof ElasticSearchV2Observation);
		ElasticSearchV2Observation actualObservation = (ElasticSearchV2Observation) observations.get(0);
		Assert.assertTrue(actualObservation.getDocumentsToFetch().get(0).getEsData().size()>0);
	}
}
