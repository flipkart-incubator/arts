package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.DirectInput;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.http.HttpDirectInput;
import com.flipkart.component.testing.model.http.HttpObservation;
import com.flipkart.component.testing.model.http.METHOD;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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

import static com.flipkart.component.testing.shared.ObjectFactory.OBJECT_MAPPER;
import static org.mockito.Mockito.mock;

//these tests fail after the introducing auto shutdown: however this SolrEmbededTest depends on mock server after the SolrEmbededTest run.
//keeping the tests for debugging purpose.
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

        Map map = OBJECT_MAPPER.readValue(specificationStr, Map.class);


        DirectInput directInput = new HttpDirectInput("/abc", METHOD.GET,null, Maps.newHashMap());
        IndirectInput indirectInput = new HttpIndirectInput(map);

        TestSpecification testSpecification = new TestSpecification(null, directInput, Lists.newArrayList(indirectInput), Lists.newArrayList(new HttpObservation()), null);

        System.out.println(OBJECT_MAPPER.writeValueAsString(testSpecification));
        List<Observation> observations = new HttpTestOrchestrator(() -> "http://localhost:7777", new HttpTestRunner() ).run(testSpecification);

        Assert.assertTrue(observations.size() == 1);
        HttpObservation httpObservation = (HttpObservation) observations.get(0);
        Map responseAsMap = httpObservation.getResponseAsMap();
        Assert.assertEquals("b", responseAsMap.get("a"));


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

        Map map = OBJECT_MAPPER.readValue(specificationStr, Map.class);

        DirectInput directInput = new HttpDirectInput("/abc", METHOD.GET,null, Maps.newHashMap());
        IndirectInput indirectInput = new HttpIndirectInput(map);


        TestSpecification testSpecification = new TestSpecification(null, directInput, Lists.newArrayList(indirectInput), Lists.newArrayList(new HttpObservation()), null);
        List<Observation> observations = new HttpTestOrchestrator(mock(HttpTestRunner.class)).run(testSpecification);
        Assert.assertTrue(observations.size() == 1);
        HttpObservation httpObservation = (HttpObservation) observations.get(0);
        Map responseAsMap = httpObservation.getResponseAsMap();
        Assert.assertEquals("b", responseAsMap.get("a"));

    }
}
