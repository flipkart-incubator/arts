package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlIndirectInput;
import com.flipkart.component.testing.model.mysql.MysqlObservation;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class MysqlTest {

    @Test
    public void testRemote() throws Exception {
        TestSpecification testSpecification = new ObjectMapper().readValue(new File("src/test/resources/testRemote.json"),TestSpecification.class);
        List<Observation> observations = new SpecificationRunner(()-> null).runLite(testSpecification);
        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue( observations.get(0) instanceof MysqlObservation);
    }

    @Test
    public void testInMemory() throws Exception {
        TestSpecification testSpecification = new ObjectMapper().readValue(new File("src/test/resources/testInMemory.json"),TestSpecification.class);
        List<Observation> observations = new SpecificationRunner(()-> null).runLite(testSpecification);
        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue( observations.get(0) instanceof MysqlObservation);
    }
}
