package com.flipkart.component.testing.loaders;

import com.flipkart.component.testing.servers.ElasticSearchLocalServer;
import com.flipkart.component.testing.model.ElasticSearchIndirectInput;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.cluster.metadata.AliasAction;

public class ElasticSearchDataLoader implements TestDataLoader<ElasticSearchIndirectInput> {

	/**
	 * loads the elasticSearchIndirectInput(indexname,typename, esdata) into Hbase
	 *
	 * @param elasticSearchIndirectInput
	 */
	@Override
	public void load(ElasticSearchIndirectInput elasticSearchIndirectInput) {
		String defaultMapping =
				"{\"_default_\" : {" +
						"\"dynamic_templates\" : [ {" +
						"\"setNotAnalyzed\" : {" +
						"\"match\" : \"*\"," +
						"\"match_mapping_type\" : \"*\"," +
						"\"mapping\" : {" +
						"\"index\" : \"not_analyzed\"" +
						"}}} ]}}";
		ElasticSearchLocalServer.client.admin().indices().create(
				new CreateIndexRequest(elasticSearchIndirectInput.getIndexName()).mapping("_default_", defaultMapping))
				.actionGet();
		ElasticSearchLocalServer.client.admin().indices().prepareAliases()
				.addAliasAction(AliasAction.newAddAliasAction(elasticSearchIndirectInput.getIndexName(), "wsr"))
				.execute().actionGet();
		ElasticSearchLocalServer.client.admin().indices().preparePutMapping(elasticSearchIndirectInput.getIndexName())
				.setType(elasticSearchIndirectInput.getTypeName()).setSource().get();
		ElasticSearchLocalServer.client.prepareIndex(elasticSearchIndirectInput.getIndexName(), elasticSearchIndirectInput.getTypeName(), "l3")
				.setSource(elasticSearchIndirectInput.getEsData()).execute().actionGet();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
