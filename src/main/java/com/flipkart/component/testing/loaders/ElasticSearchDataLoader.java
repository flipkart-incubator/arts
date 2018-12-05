package com.flipkart.component.testing.loaders;


import com.flipkart.component.testing.Utils;
import com.flipkart.component.testing.model.DocumentsOfIndexAndType;
import com.flipkart.component.testing.model.ElasticSearchIndirectInput;
import com.flipkart.component.testing.model.ElasticSearchObservation;
import com.flipkart.component.testing.shared.ObjectFactory;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ElasticSearchDataLoader implements TestDataLoader<ElasticSearchIndirectInput> {

    /**
     * loads the elasticSearchIndirectInput(indexname,typename, esdata) into Hbase
     *
     * @param elasticSearchIndirectInput
     */
    @Override
    public void load(ElasticSearchIndirectInput elasticSearchIndirectInput) {
        Client client = ObjectFactory.getESOperations(elasticSearchIndirectInput).getClient();

        // is this way of id generation ok ??
        int id = 1;
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        createMapping(elasticSearchIndirectInput.getDocumentsOfIndexAndType(), client);

        for (DocumentsOfIndexAndType documentsOfIndexAndType : elasticSearchIndirectInput.getDocumentsOfIndexAndType()) {
            for (Map<String, Object> data : documentsOfIndexAndType.getDocuments()) {

                IndexRequestBuilder indexRequestBuilder = client.prepareIndex(documentsOfIndexAndType.getIndex(), documentsOfIndexAndType.getType(), String.valueOf(id)).setSource(data);

                //add routingKey If Present
                Optional.ofNullable(documentsOfIndexAndType.getRoutingKey()).ifPresent(rk -> indexRequestBuilder.setRouting(data.get(rk).toString()));
                bulkRequestBuilder.add(indexRequestBuilder);
                id = id + 1;
            }
        }
        BulkResponse bulkItemResponses = bulkRequestBuilder.execute().actionGet();

        String[] indices = elasticSearchIndirectInput.getDocumentsOfIndexAndType().stream().map(DocumentsOfIndexAndType::getIndex).toArray(String[]::new);
        client.admin().indices().prepareRefresh(indices);
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
    private void createMapping(List<DocumentsOfIndexAndType> documentsOfIndexAndTypeList, Client client) {
        for (DocumentsOfIndexAndType documentsOfIndexAndType : documentsOfIndexAndTypeList) {
            String mappingFileContent = Utils.getFileString(documentsOfIndexAndType.getMappingFile());
            String index = documentsOfIndexAndType.getIndex();
            String type = documentsOfIndexAndType.getType();
            IndicesExistsResponse indicesExistsResponse = client.admin().indices().exists(new IndicesExistsRequest().indices(new String[]{index})).actionGet();

            if (indicesExistsResponse.isExists()) {
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest().indices(index);
                client.admin().indices().delete(deleteIndexRequest).actionGet();
            }
            client.admin().indices().create(new CreateIndexRequest(index).mapping(type, mappingFileContent)).actionGet();
        }

    }

}



