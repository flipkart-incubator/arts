package com.flipkart.component.testing.loaders;


import com.flipkart.component.testing.model.solr.SolrIndirectInput;
import com.flipkart.component.testing.shared.ObjectFactory;
import com.flipkart.component.testing.shared.SolrOperations;
import org.apache.solr.common.SolrInputDocument;

import java.util.Map;

public class SolrDataLoader implements TestDataLoader<SolrIndirectInput> {

    /**
     * loads the solrIndirectInput(documents into the respective core) into Hbase
     *
     * @param solrIndirectInput
     */
    @Override
    public void load(SolrIndirectInput solrIndirectInput) {
        SolrOperations solrOperations = ObjectFactory.getSolrOperations(solrIndirectInput);


        for (Map<String, Object> document : solrIndirectInput.getSolrData().getDocuments()) {
            SolrInputDocument solrDocument = new SolrInputDocument();
            for (String key : document.keySet()) {
                solrDocument.addField(key, document.get(key));
            }
            solrOperations.addSolrDocument(solrDocument);
            solrOperations.commit();

        }

    }
}



