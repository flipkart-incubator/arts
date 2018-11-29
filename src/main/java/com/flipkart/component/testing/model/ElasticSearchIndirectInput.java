package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

/**
 * Elastic search Indirect Input
 */
@JsonTypeName("elasticSearchIndirectInput")
@Getter
public class ElasticSearchIndirectInput implements IndirectInput {
	/**
	 * The name of the index,type to load the data to
	 */
	private final String indexName;
	private final String typeName;

	/**
	 * Map of data which needs to be loaded to elastic search
	 */
	private final Map<String, Object> esData;

	@JsonCreator
	public ElasticSearchIndirectInput(@JsonProperty("indexName") String indexName,
			@JsonProperty("typeName") String typeName, @JsonProperty("data") Map<String, Object> esData) {

		this.indexName = indexName;
		this.typeName = typeName;
		this.esData = esData;
	}

}
