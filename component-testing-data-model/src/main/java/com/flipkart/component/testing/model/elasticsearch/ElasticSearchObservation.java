package com.flipkart.component.testing.model.elasticsearch;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.internal.Constants;
import com.flipkart.component.testing.model.Observation;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Elastic search Observation
 * Observation and Indirect Input for all classes
 */
@JsonTypeName("elasticSearchObservation")
@Getter
public class ElasticSearchObservation implements Observation, ElasticSearchTestConfig {

	private final List<DocumentsToFetch> documentsToFetch;
	private final ConnectionInfo connectionInfo;

	@JsonCreator
	public ElasticSearchObservation(@JsonProperty("documentsToFetch") List<DocumentsToFetch> documentsToFetch,
									@JsonProperty("connectionInfo") ConnectionInfo connectionInfo) {

		this.documentsToFetch = documentsToFetch;
		this.connectionInfo = connectionInfo;
		if(connectionInfo!=null && (connectionInfo.getConnectionType()!=ConnectionType.IN_MEMORY)){
			documentsToFetch.forEach(document-> {
				if (!document.getIndex().contains("regression_"))
					throw new IllegalArgumentException("Index name should have prefix 'regression_' for REMOTE connections");
			});
		}

	}

	public ElasticSearchObservation(List<DocumentsToFetch> documentsToFetch) {
		this(documentsToFetch, null);
	}

	@Override
	public String getClusterName() {
		return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getClusterName).orElse(Constants.ES_CLUSTER_NAME);
	}

	@Override
	public String getHost() {
		return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getHost).orElse(Constants.ES_CLIENT_HOST);
	}

	@Override
	public ConnectionType getConnectionType() {
		return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getConnectionType).orElse(ConnectionType.IN_MEMORY);
	}

	@Override
	public String getClusterDownloadURL() {
		return Optional.ofNullable(connectionInfo).map(ConnectionInfo::getDownloadURL).orElse(null);
	}

	@Data
	public static class DocumentsToFetch {
		private String index;
		private final String typeName;
		private final String routingKey;
		private final String queryFile;
		private List<Map<String, Object>> esData;



		@JsonCreator
		public DocumentsToFetch(@JsonProperty("index") String index,
								@JsonProperty("typeName") String typeName,
								@JsonProperty("routingKey") String routingKey,
								@JsonProperty("queryFile") String queryFile
		){
			this.index = index;
			this.typeName = typeName;
			this.routingKey = routingKey;
			this.queryFile = queryFile;
			this.esData = new ArrayList<>();
		}

		public DocumentsToFetch(String indexName, String typeName, String routingKey, String queryFile, List<Map<String,Object>> esData){
			this(indexName, typeName, routingKey, queryFile);
			this.esData = esData;
		}

		public String getIndexName(){
			return index;
		}

	}

}

