package com.flipkart.component.testing.loaders;


import com.flipkart.component.testing.internal.Utils;
import com.flipkart.component.testing.model.elasticsearch.DocumentsOfIndexAndType;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchIndirectInput;
import com.flipkart.component.testing.shared.ElasticSearchOperations;
import com.flipkart.component.testing.shared.ObjectFactory;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

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
        ElasticSearchOperations esOperations = ObjectFactory.getESOperations(elasticSearchIndirectInput);
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

        String[] indices = elasticSearchIndirectInput.getDocumentsOfIndexAndType().stream().map(DocumentsOfIndexAndType::getIndex).toArray(String[]::new);
        esOperations.refresh(indices);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
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



