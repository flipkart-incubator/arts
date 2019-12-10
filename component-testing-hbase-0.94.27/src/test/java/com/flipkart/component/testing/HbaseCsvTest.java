package com.flipkart.component.testing;

import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.hbase.HBaseObservation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class HbaseCsvTest {

    private static SpecificationGenerator specificationGenerator;
    private static String testDataFile = "src/test/resources/hbaseTestDataFile.csv";
    private static String apiTemplateFile = "src/test/resources/hbaseApiTemplateFile.json";

    @BeforeClass
    public static void initialize() throws IOException {
        specificationGenerator = new SpecificationGenerator(testDataFile,
                new String(Files.readAllBytes(Paths.get(apiTemplateFile)), StandardCharsets.UTF_8));
    }

    @Test
    public void test1() throws Exception {
        TestSpecification testSpecification = specificationGenerator.getTestSpecification(0);

        List<Observation> observations = new HttpTestOrchestrator(()-> null).runLite(testSpecification);

        Assert.assertTrue(observations.size() == 1);
        Assert.assertTrue(observations.get(0) instanceof HBaseObservation);
        HBaseObservation actualObservation = (HBaseObservation) observations.get(0);

        Assert.assertTrue(actualObservation.getTableName().equalsIgnoreCase("regression_renamed"));
        Assert.assertEquals(3, actualObservation.getRows().size());
        Assert.assertTrue(actualObservation.getRows().get(0).getRowKey().equals("RK1_Changed"));
        Assert.assertTrue(actualObservation.getRows().get(1).getRowKey().equals("ROW_3"));

    }
}
