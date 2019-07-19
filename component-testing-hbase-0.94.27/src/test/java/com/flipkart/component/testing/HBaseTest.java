package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.hbase.HBaseObservation;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class HBaseTest {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void test() throws Exception {
		TestSpecification testSpecification = objectMapper.readValue(
				this.getClass().getClassLoader().getResourceAsStream("hbaseTestData.json"), TestSpecification.class);

		List<Observation> observations = new HttpTestOrchestrator(()-> null).runLite(testSpecification);

		Assert.assertTrue(observations.size() == 1);
		Assert.assertTrue(observations.get(0) instanceof HBaseObservation);
		HBaseObservation actualObservation = (HBaseObservation) observations.get(0);


		Assert.assertTrue(observations.size() == 1);
		Assert.assertTrue(observations.get(0) instanceof HBaseObservation);
		Assert.assertEquals("regression_t1", actualObservation.getTableName());
	}

}
