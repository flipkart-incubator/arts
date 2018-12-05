package com.flipkart.component.testing.orchestrators;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.HttpTestRunner;
import com.flipkart.component.testing.model.AerospikeIndirectInput;
import com.flipkart.component.testing.model.AerospikeObservation;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestData;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;;
import org.junit.Test;
import com.google.common.collect.Lists;
import static org.mockito.Mockito.mock;



public class AerospikeTest {


    @Test
    public void aerospikePutGetTest() throws Exception {

        File indirectInputJsonFile = new File("src/main/resources/AerospikeIndirectInput.json");
        String inidrectInputStr = FileUtils.readFileToString(indirectInputJsonFile, "UTF-8");
        AerospikeIndirectInput aerospikeIndirectInput = new ObjectMapper().readValue(inidrectInputStr,
                AerospikeIndirectInput.class);

        File observationJsonFile = new File("src/main/resources/AerospikeObservation.json");
        String observationStr = FileUtils.readFileToString(observationJsonFile, "UTF-8");
        AerospikeObservation expectedObservation = new ObjectMapper().readValue(observationStr, AerospikeObservation.class);
        TestData testData = new TestData(null, Lists.newArrayList(aerospikeIndirectInput),
                Lists.newArrayList(expectedObservation));

        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testData, () -> "");

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof AerospikeObservation);

        AerospikeObservation actualObservation = (AerospikeObservation) observations.get(0);
        Assert.assertEquals(expectedObservation.getData(), actualObservation.getData());

    }

}
