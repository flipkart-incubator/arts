package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.hbase.HBaseIndirectInput;
import com.flipkart.component.testing.model.http.HttpDirectInput;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.rmq.RMQObservation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class SpecificationGeneratorTest {
    private TestSpecification testSpecification;
    private static SpecificationGenerator specificationGenerator;

    @BeforeClass
    public static void loadtestTemplate() throws Exception {
        String csvFilePath = "src/test/resources/testDataFile.csv";
        String specificationTemplate = "src/test/resources/apiTemplateFile.json";
        specificationGenerator =  new SpecificationGenerator(csvFilePath,
                new String(Files.readAllBytes(Paths.get(specificationTemplate)), StandardCharsets.UTF_8));
    }

/*
*inputs : httpDirectInput, httpIndirectInput,hbaseIndirectInput
* observations: httpObservation,rmqObservation
*Changes on the specification : HttpDirectInput : json variables, endpoint
*                               HttpIndirectInput : response variables
*                               HBaseIndirectInput : connectionType, rowkey, complete second row, configurations
*                               RmqObservation : queue name , messages
*/
    @Test
    public void test1() {

        testSpecification = specificationGenerator.getTestSpecification(0);

        Assert.assertEquals("/path_changed", ((HttpDirectInput) testSpecification.getDirectInput()).getPath());
        Assert.assertEquals("v2_changed_test1", ((HashMap) ((HttpDirectInput) testSpecification.getDirectInput()).getRequest()).get("t3"));
        Assert.assertTrue(((HashMap)((HttpIndirectInput) testSpecification.getIndirectInputs().get(0)).getSpecification().get("response")).get("body")
                .toString().equalsIgnoreCase("{\"k1\":\"v1\",\"k2\":{\"test1\":\"v2_changed\"}}"));

        Assert.assertEquals(((HBaseIndirectInput) testSpecification.getIndirectInputs().get(1)).getConnectionType(), ConnectionType.IN_MEMORY);
        Assert.assertEquals("changeKey1", ((HBaseIndirectInput) testSpecification.getIndirectInputs().get(1)).getRows().get(0).getRowKey());

        Assert.assertEquals("msg1", ((RMQObservation) testSpecification.getObservations().get(1)).getMessages().get(0));
        Assert.assertEquals("message1", ((RMQObservation) testSpecification.getObservations().get(1)).getMessages().get(1));

    }

    /*
     *inputs : httpDirectInput, httpIndirectInput,hbaseIndirectInput
     * observations: httpObservation,rmqObservation
     *Changes on the specification : HttpDirectInput : json variables, endpoint
     *                               HttpIndirectInput : response variables
     *                               HBaseIndirectInput : connectionType, rowkey, complete second row, configurations
     *                               RmqObservation : queue name , messages
     */
    @Test
    public void test2() {

        testSpecification = specificationGenerator.getTestSpecification(1);

        Assert.assertEquals("v2_changed_test2", ((HashMap) ((HttpDirectInput) testSpecification.getDirectInput()).getRequest()).get("t3"));

        Assert.assertEquals(((HBaseIndirectInput) testSpecification.getIndirectInputs().get(1)).getConnectionType(), ConnectionType.REMOTE);
        Assert.assertEquals("changeKey2", ((HBaseIndirectInput) testSpecification.getIndirectInputs().get(1)).getRows().get(0).getRowKey());

        Assert.assertEquals("msg2", ((RMQObservation) testSpecification.getObservations().get(1)).getMessages().get(0));
        Assert.assertEquals("message2", ((RMQObservation) testSpecification.getObservations().get(1)).getMessages().get(1));

    }

    /*
     *inputs : httpDirectInput, httpIndirectInput,hbaseIndirectInput
     * observations: httpObservation,rmqObservation
     *Changes on the specification : HttpDirectInput : json variables, endpoint
     *                               HttpIndirectInput : response variables
     *                               HBaseIndirectInput : connectionType, rowkey, complete second row, configurations
     *                               RmqObservation : queue name , messages
     */
    @Test
    public void test3() throws IOException {

        testSpecification = specificationGenerator.getTestSpecification(2);

        Assert.assertEquals("v2_changed_test3", ((HashMap) ((HttpDirectInput) testSpecification.getDirectInput()).getRequest()).get("t3"));

        Assert.assertEquals(((HBaseIndirectInput) testSpecification.getIndirectInputs().get(1)).getConnectionType(), ConnectionType.IN_MEMORY);
        Assert.assertEquals("replaced_rowKey", ((HBaseIndirectInput) testSpecification.getIndirectInputs().get(1)).getRows().get(0).getRowKey());

        String newRowData = "{\"colFam1\": {\"k1\": \"replaced_v1\",\"k2\": \"replaced_v2\"}}";
        Assert.assertEquals(((HBaseIndirectInput) testSpecification.getIndirectInputs().get(1)).getRows().get(0).getData(), new ObjectMapper().readValue(newRowData, HashMap.class));
        Assert.assertEquals("msg3", ((RMQObservation) testSpecification.getObservations().get(1)).getMessages().get(0));
        Assert.assertEquals("message3", ((RMQObservation) testSpecification.getObservations().get(1)).getMessages().get(1));

    }


}
