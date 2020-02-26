package service.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.http.HttpObservation;
import org.junit.Assert;
import org.junit.Test;
import test.Runner.SuiteSetUp;

import java.io.File;
import java.util.List;

public class HelloTest extends SuiteSetUp {
    List<Observation> observations;

    @Test
    public void helloTest() throws Exception {
        String specFile = "src/test/resources/test.json";
        TestSpecification testSpecification = new ObjectMapper().readValue(new File(specFile).getAbsoluteFile(),TestSpecification.class);

        observations = specificationRunner.runLite(testSpecification);

        Assert.assertEquals(200,((HttpObservation)observations.get(0)).getStatuscode());

        String expectedResponse = "ARTS SAYS HELLO !!!";
        Assert.assertEquals(expectedResponse,((HttpObservation)observations.get(0)).getResponseAsMap().get("message"));

    }

}
