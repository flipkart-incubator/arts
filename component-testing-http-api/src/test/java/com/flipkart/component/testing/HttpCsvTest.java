package com.flipkart.component.testing;

import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.http.HttpObservation;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class HttpCsvTest {

    private static SpecificationRunner specificationRunner;
    private TestSpecification testSpecification;
    private static SpecificationGenerator specificationGenerator;

    @BeforeClass
    public static void initialize() throws IOException {
        String testDataFile = "src/test/resources/httpTestDataFile.csv";
        String apiTemplateFile = "src/test/resources/httpApiTemplateFile.json";
        specificationGenerator = new SpecificationGenerator(testDataFile,
                new String(Files.readAllBytes(Paths.get(apiTemplateFile)), StandardCharsets.UTF_8));
        specificationRunner = new SpecificationRunner(() -> "http://localhost:7777");
    }

    /*
    *inputs : httpIndirectInput
    * observations : httpObservation
    * changes in spec : directInput : path
    *                   indirectInput : response variables
    */

    @Test
    public void test1WithCsv() throws Exception {
        testSpecification = specificationGenerator.getTestSpecification(0);
        List<Observation> observations = specificationRunner.runLite(testSpecification);
        Assert.assertNotNull(observations.get(0));
        Assert.assertEquals(200,((HttpObservation) observations.get(0)).getStatuscode());
        Assert.assertEquals(((HttpObservation) observations.get(0)).getResponseAsMap().get("k4"), Lists.newArrayList("k1_new", "k2_new"));
    }
}
