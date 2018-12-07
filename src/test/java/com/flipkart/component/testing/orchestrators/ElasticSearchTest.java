package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchObservation;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

public class ElasticSearchTest {


	@Test
	public void test() throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		TestSpecification testSpecification = objectMapper.readValue(this.getClass().getClassLoader().getResourceAsStream("es-test-data.json"), TestSpecification.class);
		List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification, () -> "");
		Assert.assertTrue(observations.size() == 1);
		Assert.assertTrue(observations.get(0) instanceof ElasticSearchObservation);
		ElasticSearchObservation actualObservation = (ElasticSearchObservation) observations.get(0);
		Assert.assertTrue(actualObservation.getDocumentsToFetch().get(0).getEsData().size()>0);
	}
}
