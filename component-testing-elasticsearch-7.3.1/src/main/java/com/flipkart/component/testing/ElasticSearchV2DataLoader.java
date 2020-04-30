package com.flipkart.component.testing;


import com.flipkart.component.testing.model.elasticsearch.Document;
import com.flipkart.component.testing.model.elasticsearch.DocumentsOfIndexAndType;
import com.flipkart.component.testing.model.elasticsearch.v2.ElasticSearchV2IndirectInput;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.*;

public class ElasticSearchV2DataLoader implements TestDataLoader<ElasticSearchV2IndirectInput> {

    /**
     * loads the elasticSearchIndirectInput(indexname,typename, esdata) into Elasticsearch using rest client
     *
     * @param elasticSearchIndirectInput
     */
    @Override
    public void load(ElasticSearchV2IndirectInput elasticSearchIndirectInput) {
        ElasticSearchV2Operations esOperations = ESV2Factory.getESOperations(elasticSearchIndirectInput);
        RestHighLevelClient client = esOperations.getClient();

        BulkRequest bulkRequest = new BulkRequest();

        createMapping(elasticSearchIndirectInput.getDocumentsOfIndexAndType(), esOperations);

        for (DocumentsOfIndexAndType documentsOfIndexAndType : elasticSearchIndirectInput.getDocumentsOfIndexAndType()) {
            for (Document document : documentsOfIndexAndType.getDocuments()) {
                IndexRequest indexRequest = validateAndCreateIndexRequest(documentsOfIndexAndType, document);
                bulkRequest.add(indexRequest);
            }
        }
        executeBulkRequest(client, bulkRequest);
        String[] indices = elasticSearchIndirectInput.getDocumentsOfIndexAndType()
                .stream()
                .map(DocumentsOfIndexAndType::getIndex)
                .toArray(String[]::new);
        esOperations.refresh(indices);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Executes bulk request with the using the rest client given
     * @param client
     * @param bulkRequest
     */
    private void executeBulkRequest(RestHighLevelClient client, BulkRequest bulkRequest) {
        BulkResponse bulkItemResponses;
        try {
            bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Failed to execute bulk request " + e.getMessage() + " " +
                    Arrays.toString(e.getStackTrace()));
        }
        if(bulkItemResponses.hasFailures())
            throw new RuntimeException("Failed to execute bulk request " +bulkItemResponses.buildFailureMessage());
    }

    private IndexRequest validateAndCreateIndexRequest(DocumentsOfIndexAndType documentsOfIndexAndType, Document document) {
        if(document.getDocumentId()==null)
            throw new IllegalArgumentException("id(document Id) must be present for each document");
        Map<String,Object> data = document.getData();
        //add routingKey If Present
        return new IndexRequest(documentsOfIndexAndType.getIndex())
                .source(data)
                .id(document.getDocumentId())
                .routing(Optional.ofNullable(data.get(documentsOfIndexAndType.getRoutingKey()))
                        .map(Object::toString).orElse(null));
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<ElasticSearchV2IndirectInput>> getIndirectInputClasses() {
        return Collections.singletonList(ElasticSearchV2IndirectInput.class);
    }

    /**
     * create mapping for types that belong to index
     *
     * @param documentsOfIndexAndTypeList
     */
    private void createMapping(List<DocumentsOfIndexAndType> documentsOfIndexAndTypeList, ElasticSearchV2Operations esOperations) {
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



