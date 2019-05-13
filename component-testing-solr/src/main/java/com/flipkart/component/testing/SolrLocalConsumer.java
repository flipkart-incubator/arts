package com.flipkart.component.testing;


import com.flipkart.component.testing.model.solr.SolrObservation;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads from the local Solr search Consumer
 */
class SolrLocalConsumer implements ObservationCollector<SolrObservation> {

    /**
     * extracts the actual Observation using attributes from expected observation
     * like tableName
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public SolrObservation actualObservations(SolrObservation expectedObservation) {
        SolrOperations solrOperations = SolrFactory.getSolrOperations(expectedObservation);
        SolrDocumentList actualDocumentList = solrOperations.getQueryResponse(expectedObservation.getSolrExpectedData().getQueryFile())
                .getResults();
        return new SolrObservation(getListOfSolrCores(expectedObservation,
                actualDocumentList), expectedObservation.getConnectionType());
    }

    @Override
    public Class<SolrObservation> getObservationClass() {
        return SolrObservation.class;
    }

    private SolrObservation.SolrExpectedData getListOfSolrCores(SolrObservation solrObservation, SolrDocumentList actualDocumentList) {
            List actualDocumentListInaCore = new ArrayList();
            for (SolrDocument actualDoc : actualDocumentList) {
                Map<String, String> actualDocument = new HashMap<>();
                for (Object key : actualDoc.getFieldValueMap().keySet()) {
                    actualDocument.put(key.toString(), actualDoc.get(key).toString());
                }
                actualDocumentListInaCore.add(actualDocument);
            }
        return new SolrObservation.SolrExpectedData(solrObservation.getSolrExpectedData().getCoreName(),
                solrObservation.getSolrExpectedData().getQueryFile(),actualDocumentListInaCore);
    }
}