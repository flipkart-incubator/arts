package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.HttpTestRunner;
import com.flipkart.component.testing.model.ElasticSearchObservation;
import com.flipkart.component.testing.model.TestData;
import com.flipkart.component.testing.servers.ElasticSearchLocalServer;
import com.google.common.collect.Lists;
import com.flipkart.component.testing.model.ElasticSearchIndirectInput;
import com.flipkart.component.testing.model.Observation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.mockito.Mockito.mock;

public class ElasticSearchTest {


	@Test
	public void test() throws Exception {

		String inidrectInputStr = "{\"name\": \"elasticSearchIndirectInput\",\"indexName\":\"index1\",\"typeName\":\"type1\",\"data\":{\"K1\":\"V1\",\"K2\":\"V2\"}}";
		ElasticSearchIndirectInput elasticSearchIndirectInput = new ObjectMapper().readValue(inidrectInputStr,
				ElasticSearchIndirectInput.class);

		String observationStr = "{\"name\": \"elasticSearchObservation\",\"indexName\":\"index1\",\"typeName\":\"type1\",\"data\":{\"K1\":\"V1\",\"K2\":\"V2\"}}";
		ElasticSearchObservation expectedObservation = new ObjectMapper().readValue(observationStr,
				ElasticSearchObservation.class);

		TestData testData = new TestData(null, Lists.newArrayList(elasticSearchIndirectInput),
				Lists.newArrayList(expectedObservation));

		List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testData, () -> "");

		Assert.assertTrue(observations.size() == 1);
		Assert.assertTrue(observations.get(0) instanceof ElasticSearchObservation);
		ElasticSearchObservation actualObservation = (ElasticSearchObservation) observations.get(0);
		Assert.assertEquals(expectedObservation.getIndexName(), actualObservation.getIndexName());
		Assert.assertEquals(expectedObservation.getEsData(), actualObservation.getEsData());

	}

	@After
	public void after() {
		ElasticSearchLocalServer.client.close();
		ElasticSearchLocalServer.cluster.stop();

	}
}
