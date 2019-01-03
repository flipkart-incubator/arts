package com.flipkart.component.testing.extractors;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchObservation;
import com.flipkart.component.testing.shared.ElasticSearchOperations;
import com.flipkart.component.testing.shared.ObjectFactory;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Reads from the local Elastic search Consumer
 */
class ElasticSearchLocalConsumer implements ObservationCollector<ElasticSearchObservation> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * extracts the actual Observation using attributes from expected observation
     * like tableName
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public ElasticSearchObservation actualObservations(ElasticSearchObservation expectedObservation) {
        ElasticSearchOperations esOperations = ObjectFactory.getESOperations(expectedObservation);
        Client client = esOperations.getClient();
        refresh(expectedObservation, esOperations);
        List<ElasticSearchObservation.DocumentsToFetch> documents = new ArrayList<>();
        try {
            for (ElasticSearchObservation.DocumentsToFetch documentsToFetch : expectedObservation.getDocumentsToFetch()) {
                Map map = objectMapper.readValue(this.getClass().getClassLoader().getResourceAsStream(documentsToFetch.getQueryFile()), Map.class);
                SearchRequestBuilder searchRequestBuilder = client.prepareSearch(documentsToFetch.getIndexName()).setTypes(documentsToFetch.getTypeName()).setSource(map);

                Optional.ofNullable(documentsToFetch.getRoutingKey()).ifPresent(searchRequestBuilder::setRouting);
                SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
                System.out.println(searchResponse);
                ElasticSearchObservation.DocumentsToFetch response = prepareResponseDoc(searchResponse, documentsToFetch.getRoutingKey(), documentsToFetch.getQueryFile());
                documents.add(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return new ElasticSearchObservation(documents, expectedObservation.getConnectionInfo());
    }

    private void refresh(ElasticSearchObservation expectedObservation, ElasticSearchOperations esOperations) {
        String[] indices = expectedObservation.getDocumentsToFetch().stream().map(ElasticSearchObservation.DocumentsToFetch::getIndexName).toArray(String[]::new);
        esOperations.refresh(indices);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

    private ElasticSearchObservation.DocumentsToFetch prepareResponseDoc(SearchResponse searchResponse, String routingKey, String queryFile) {

        String actualIndex = null, actualType = null;
        List<Map<String, Object>> actaulEsDataList = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            actualIndex = hit.getIndex();
            actualType = hit.getType();
            actaulEsDataList.add(hit.getSource());
        }
        return new ElasticSearchObservation.DocumentsToFetch(actualIndex, actualType, routingKey, queryFile, actaulEsDataList);
    }


}
