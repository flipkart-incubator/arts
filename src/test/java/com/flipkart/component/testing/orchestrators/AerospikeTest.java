package com.flipkart.component.testing.orchestrators;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.aerospike.AerospikeIndirectInput;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.solr.SolrObservation;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;;
import org.junit.Ignore;
import org.junit.Test;
import com.google.common.collect.Lists;

import static com.flipkart.component.testing.shared.ObjectFactory.OBJECT_MAPPER;
import static org.mockito.Mockito.mock;



public class AerospikeTest {


    @Test
    public void aerospikePutGetTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TestSpecification testSpecification = mapper
                .readValue(this.getClass().getClassLoader().getResourceAsStream("aerospikeTestData.json"), TestSpecification.class);
        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification);
        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof AerospikeObservation);
        AerospikeObservation actualObservation = (AerospikeObservation) observations.get(0);
        Assert.assertEquals(testSpecification.getObservations().get(0),actualObservation);
    }

}
