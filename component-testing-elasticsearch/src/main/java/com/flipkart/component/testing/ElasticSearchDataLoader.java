package com.flipkart.component.testing;


import com.flipkart.component.testing.model.elasticsearch.DocumentsOfIndexAndType;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchIndirectInput;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ElasticSearchDataLoader implements TestDataLoader<ElasticSearchIndirectInput> {

    /**
     * loads the elasticSearchIndirectInput(indexname,typename, esdata) into Hbase
     *
     * @param elasticSearchIndirectInput
     */
    @Override
    public void load(ElasticSearchIndirectInput elasticSearchIndirectInput) {
        ElasticSearchOperations esOperations = ESFactory.getESOperations(elasticSearchIndirectInput);
        Client client = esOperations.getClient();

        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        createMapping(elasticSearchIndirectInput.getDocumentsOfIndexAndType(), client, esOperations);

        for (DocumentsOfIndexAndType documentsOfIndexAndType : elasticSearchIndirectInput.getDocumentsOfIndexAndType()) {
            for (Map<String, Object> data : documentsOfIndexAndType.getDocuments()) {

                if(!data.containsKey("_id")) throw new IllegalArgumentException("_id must be present for each document");
                IndexRequestBuilder indexRequestBuilder = client.prepareIndex(documentsOfIndexAndType.getIndex(), documentsOfIndexAndType.getType(), String.valueOf(data.get("_id"))).setSource(data);

                //add routingKey If Present
                Optional.ofNullable(documentsOfIndexAndType.getRoutingKey()).ifPresent(rk -> indexRequestBuilder.setRouting(data.get(rk).toString()));
                bulkRequestBuilder.add(indexRequestBuilder);
            }
        }
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();
        if(bulkItemResponses.hasFailures())
            throw new RuntimeException("Failed to execute bulk request " +bulkItemResponses.buildFailureMessage());

        String[] indices = elasticSearchIndirectInput.getDocumentsOfIndexAndType().stream().map(DocumentsOfIndexAndType::getIndex).toArray(String[]::new);
        esOperations.refresh(indices);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<ElasticSearchIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(ElasticSearchIndirectInput.class);
    }

    /**
     * create mapping for types that belong to index
     *
     * @param documentsOfIndexAndTypeList
     * @param client
     */
    private void createMapping(List<DocumentsOfIndexAndType> documentsOfIndexAndTypeList, Client client, ElasticSearchOperations esOperations) {
        for (DocumentsOfIndexAndType documentsOfIndexAndType : documentsOfIndexAndTypeList) {
            String mappingFileContent = Utils.getFileString(documentsOfIndexAndType.getMappingFile());
            String index = documentsOfIndexAndType.getIndex();
            String type = documentsOfIndexAndType.getType();
            if (esOperations.isIndexPresent(index)) {
                throw new IllegalStateException("index is already present before loading the data, this should not be the case");
            }
            esOperations.createIndex(index, type, mappingFileContent);
        }

    }

}



