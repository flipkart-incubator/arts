package com.flipkart.component.testing.model.elasticsearch.v2;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.internal.Constants;
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
@JsonTypeName("elasticSearchV2Observation")
@Getter
public class ElasticSearchV2Observation extends ElasticSearchV2Config implements Observation {

	private final List<DocumentsToFetch> documentsToFetch;

	@JsonCreator
	public ElasticSearchV2Observation(@JsonProperty("documentsToFetch") List<DocumentsToFetch> documentsToFetch,
									  @JsonProperty("connectionInfo") ConnectionInfo connectionInfo) {

		super(connectionInfo);
		this.documentsToFetch = documentsToFetch;
		if(connectionInfo!=null && (connectionInfo.getConnectionType()!=ConnectionType.IN_MEMORY)){
			documentsToFetch.forEach(document-> {
				if (!document.getIndex().contains("regression_"))
					throw new IllegalArgumentException("Index name should have prefix 'regression_' for REMOTE connections");
			});
		}
	}

	public ElasticSearchV2Observation(List<DocumentsToFetch> documentsToFetch) {
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

