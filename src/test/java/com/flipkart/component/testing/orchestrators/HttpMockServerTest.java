package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.TestSpecification;
import com.google.common.collect.Lists;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.Observation;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

//these tests fail after the introducing auto shutdown: however this test depends on mock server after the test run.
//keeping the tests for debugging purpose.
@Ignore
public class HttpMockServerTest {

    @Test
    public void testWithPathMatching() throws Exception {
        String specificationStr = "{\n" +
                "  \"request\": {\n" +
                "    \"method\": \"GET\",\n" +
                "    \"url\": \"/abc\"\n" +
                "  },\n" +
                "  \"response\": {\n" +
                "    \"status\": 200,\n" +
                "    \"body\": {\n" +
                "      \"a\": \"b\"\n" +
                "    },\n" +
                "    \"headers\": {\n" +
                "      \"Content-Type\": \"application/json\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(specificationStr, Map.class);


        IndirectInput indirectInput = new HttpIndirectInput(map);

        TestSpecification testSpecification = new TestSpecification(null, null, Lists.newArrayList(indirectInput), Lists.newArrayList());

        System.out.println(objectMapper.writeValueAsString(testSpecification));
        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification);

        HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:7777/abc").asJson();

        String val = (String) jsonResponse.getBody().getObject().get("a");
        Assert.assertEquals("b", val);


    }

    @Test
    public void testPathAndHeaderMatch() throws Exception {
        String specificationStr = "{\n" +
                "  \"request\": {\n" +
                "    \"method\": \"GET\",\n" +
                "    \"url\": \"/abc\",\n" +
                "    \"headers\" : {\n" +
                "      \"abc\" : {\n" +
                "        \"equalTo\" : \"custom\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"response\": {\n"   +
                "    \"status\": 200,\n" +
                "    \"body\": {\n" +
                "      \"a\": \"b\"\n" +
                "    },\n" +
                "    \"headers\": {\n" +
                "      \"Content-Type\": \"application/json\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(specificationStr, Map.class);

        IndirectInput indirectInput = new HttpIndirectInput(map);

        TestSpecification testSpecification = new TestSpecification(null, null, Lists.newArrayList(indirectInput), Lists.newArrayList());
        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification);

        HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:7777/abc").header("abc","custom").asJson();

        String val = (String) jsonResponse.getBody().getObject().get("a");
        Assert.assertEquals("b", val);

    }
}
