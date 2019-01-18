package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.hbase.HBaseIndirectInput;
import com.flipkart.component.testing.model.hbase.HBaseObservation;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

public class HBaseTest {

	@Test
	@Ignore
	public void test() throws Exception {

		String inidrectInputStr = "{\"name\": \"hbaseIndirectInput\",\"tableName\": \"t1\",\"connectionType\":\"IN_MEMORY\",\"rows\": [{\"rowKey\": \"rk1\",\"data\": {\"cf1\": {\"k1\": \"v1\",\"k2\": \"v2\"}}},{\"rowKey\": \"rk2\",\"data\": {\"cf1\": {\"k1\": \"v1\"}}}]}";
		HBaseIndirectInput hBaseIndirectInput = new ObjectMapper().readValue(inidrectInputStr,
				HBaseIndirectInput.class);

		String observationStr = "{\"name\":\"hbaseObservation\",\"tableName\":\"t1\",\"connectionType\":\"IN_MEMORY\",\"rows\":[{\"rowKey\":\"rk1\",\"data\":{\"cf1\":{\"k1\":\"v1\",\"k2\":\"v2\"}}},{\"rowKey\":\"rk2\",\"data\":{\"cf1\":{\"k1\":\"v1\"}}}]}";
		HBaseObservation expectedObservation = new ObjectMapper().readValue(observationStr, HBaseObservation.class);

		TestSpecification testSpecification = new TestSpecification(null, null, Lists.newArrayList(hBaseIndirectInput),
				Lists.newArrayList(expectedObservation));

		List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification);

		Assert.assertTrue(observations.size() == 1);
		Assert.assertTrue(observations.get(0) instanceof HBaseObservation);
		HBaseObservation actualObservation = (HBaseObservation) observations.get(0);
		Assert.assertEquals(expectedObservation.getTableName(), actualObservation.getTableName());
		Assert.assertEquals(expectedObservation.getRows(), actualObservation.getRows());
	}

}
