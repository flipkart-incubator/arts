package com.flipkart.component.testing;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.elasticsearch.v2.ElasticSearchV2Observation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.ContextParser;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Reads from the local Elastic search Consumer
 */
@Slf4j
class ElasticSearchV2LocalConsumer implements ObservationCollector<ElasticSearchV2Observation> {


    private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * extracts the actual Observation using attributes from expected observation
     * like tableName
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public ElasticSearchV2Observation actualObservations(ElasticSearchV2Observation expectedObservation) {
        ElasticSearchV2Operations esOperations = ESV2Factory.getESOperations(expectedObservation);
        RestHighLevelClient client = esOperations.getClient();
        refresh(expectedObservation, esOperations);
        List<ElasticSearchV2Observation.DocumentsToFetch> documents = new ArrayList<>();
        try {
            for (ElasticSearchV2Observation.DocumentsToFetch documentsToFetch : expectedObservation.getDocumentsToFetch()) {
                String queryString = objectMapper.readTree(this.getClass().getClassLoader().
                        getResourceAsStream(documentsToFetch.getQueryFile())).toString();
                Request request = new Request("POST", "_search");
                request.setJsonEntity(queryString);
                Response searchResponse = client.getLowLevelClient().performRequest(request);
                String responseString = EntityUtils.toString(searchResponse.getEntity());
                ElasticSearchV2Observation.DocumentsToFetch response =
                        prepareResponseDoc(getSearchResponseFromJson(responseString),
                                documentsToFetch.getRoutingKey(), documentsToFetch.getQueryFile());
                documents.add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new ElasticSearchV2Observation(documents, null);
    }

    @Override
    public Class<ElasticSearchV2Observation> getObservationClass() {
        return ElasticSearchV2Observation.class;
    }

    private void refresh(ElasticSearchV2Observation expectedObservation, ElasticSearchV2Operations esOperations) {
        String[] indices = expectedObservation.getDocumentsToFetch().stream().map(ElasticSearchV2Observation.DocumentsToFetch::getIndexName).toArray(String[]::new);
        esOperations.refresh(indices);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    private ElasticSearchV2Observation.DocumentsToFetch prepareResponseDoc(SearchResponse searchResponse, String routingKey, String queryFile) {

        String actualIndex = null, actualType = null;
        List<Map<String, Object>> actaulEsDataList = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            actualIndex = hit.getIndex();
            actaulEsDataList.add(hit.getSourceAsMap());
        }
        return new ElasticSearchV2Observation.DocumentsToFetch(actualIndex, null, routingKey, queryFile, actaulEsDataList);
    }

    // Use in case of aggregations
    public static List<NamedXContentRegistry.Entry> getDefaultNamedXContents() {
        Map<String, ContextParser<Object, ? extends Aggregation>> map = new HashMap<>();
        map.put(TopHitsAggregationBuilder.NAME, (p, c) -> ParsedTopHits.fromXContent(p, (String) c));
        map.put(StringTerms.NAME, (p, c) -> ParsedStringTerms.fromXContent(p, (String) c));
        return map.entrySet().stream()
                .map(entry -> new NamedXContentRegistry.Entry(Aggregation.class, new ParseField(entry.getKey()), entry.getValue()))
                .collect(Collectors.toList());
    }

    private SearchResponse getSearchResponseFromJson(String jsonResponse){
        try {
            XContentParser parser = JsonXContent.jsonXContent.createParser(NamedXContentRegistry.EMPTY,
                    DeprecationHandler.THROW_UNSUPPORTED_OPERATION,  jsonResponse);
            return SearchResponse.fromXContent(parser);
        } catch (IOException e) {
            throw new RuntimeException("Parsing search response failed for resposne " + jsonResponse);
        }
    }


}
