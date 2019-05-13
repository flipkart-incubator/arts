package com.flipkart.component.testing;


import com.flipkart.component.testing.model.solr.SolrIndirectInput;
import org.apache.solr.common.SolrInputDocument;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SolrDataLoader implements TestDataLoader<SolrIndirectInput> {

    /**
     * loads the solrIndirectInput(documents into the respective core) into Hbase
     *
     * @param solrIndirectInput
     */
    @Override
    public void load(SolrIndirectInput solrIndirectInput) {
        SolrOperations solrOperations = SolrFactory.getSolrOperations(solrIndirectInput);


        for (Map<String, Object> document : solrIndirectInput.getSolrData().getDocuments()) {
            SolrInputDocument solrDocument = new SolrInputDocument();
            for (String key : document.keySet()) {
                solrDocument.addField(key, document.get(key));
            }
            solrOperations.addSolrDocument(solrDocument);
            solrOperations.commit();

        }

    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<SolrIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(SolrIndirectInput.class);
    }
}



