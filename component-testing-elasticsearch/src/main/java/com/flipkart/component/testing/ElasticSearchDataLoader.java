package com.flipkart.component.testing;


import com.flipkart.component.testing.model.elasticsearch.Document;
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

        for (DocumentsOfIndexAndType documentsOfIndexAndType : elasticSearchIndirectInput.getDocumentsOfIndexAndType()) {
            String mappingFileContent = Utils.getFileString(documentsOfIndexAndType.getMappingFile());
            if(!esOperations.isIndexPresent(documentsOfIndexAndType.getIndex()))
                esOperations.createIndex(documentsOfIndexAndType.getIndex(),documentsOfIndexAndType.getType());
            else if(esOperations.isIndexPresent(documentsOfIndexAndType.getIndex()) && esOperations.isTypePresent(documentsOfIndexAndType.getIndex(),documentsOfIndexAndType.getType()))
                throw new RuntimeException("Type "+documentsOfIndexAndType.getType()+" is already present under the index "+documentsOfIndexAndType.getIndex());

            for (Document document: documentsOfIndexAndType.getDocuments()) {
                if(document.getDocumentId()==null) throw new IllegalArgumentException("_id(document Id) must be present for each document");
                Map<String,Object> data = document.getData();

                esOperations.executePutMapping(documentsOfIndexAndType.getIndex(),documentsOfIndexAndType.getType(),mappingFileContent);

                IndexRequestBuilder indexRequestBuilder ;
                if(document.getParentId()!=null)
                    indexRequestBuilder = client.prepareIndex(documentsOfIndexAndType.getIndex(), documentsOfIndexAndType.getType(), document.getDocumentId()).setParent(document.getParentId()).setSource(data);
                else
                    indexRequestBuilder = client.prepareIndex(documentsOfIndexAndType.getIndex(), documentsOfIndexAndType.getType(), document.getDocumentId()).setSource(data);

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

}



