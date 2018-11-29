package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.ElasticSearchObservation;
import com.flipkart.component.testing.servers.ElasticSearchLocalServer;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.util.HashMap;
import java.util.Map;

/**
 * Reads from the local Elastic search Consumer
 */
class ElasticSearchLocalConsumer implements ObservationCollector<ElasticSearchObservation> {
	/**
	 * extracts the actual Observation using attributes from expected observation
	 * like tableName
	 *
	 * @param expectedObservation
	 * @return
	 */
	@Override
	public ElasticSearchObservation actualObservations(ElasticSearchObservation expectedObservation) {
		String actualIndexName = null, actualType = null;
		Map<String, Object> actualEsData = null;
		SearchResponse searchResult = null;
		searchResult = ElasticSearchLocalServer.client.prepareSearch(expectedObservation.getIndexName()).execute()
				.actionGet();
		for (SearchHit hit : searchResult.getHits().getHits()) {
			actualEsData = new HashMap<String, Object>();
			actualIndexName = hit.getIndex();
			actualType = hit.getType();
			for (String key : hit.getSource().keySet()) {
				try {
					actualEsData.put(key, hit.getSource().get(key).toString());
				}catch (NullPointerException e){
					System.out.println("Null value at : "+key);
				}
			}

		}
		return new ElasticSearchObservation(actualIndexName, actualType, actualEsData);
	}

}
