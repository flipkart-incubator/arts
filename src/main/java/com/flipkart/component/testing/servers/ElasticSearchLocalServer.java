package com.flipkart.component.testing.servers;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

public class ElasticSearchLocalServer extends DependencyInitializer {
	//TODO :DIPIKA: instance variables should not be public
	public static EmbeddedElastic cluster;
	public static Client client;
	private static final String esClusterPort = "9210";
	private static final String esClusterVersion = "1.5.0";
	private static final String esClientHost = "127.0.0.1";
	private static final int esClientPort =9300;


	public ElasticSearchLocalServer() {
		cluster = EmbeddedElastic.builder().withSetting(PopularProperties.HTTP_PORT, esClusterPort).withElasticVersion(esClusterVersion)
				.build();
	}

	/**
	 * Starts the Elastic search cluster locally
	 */
	public void startEmbeddedElasticSearch() {
		try {
			cluster.start();
		} catch (Exception e) {
			throw new RuntimeException("Could not start Embedded ES cluster : " + e.getMessage());
		}
	}

	/**
	 * Starts the Elastic search client locally
	 */
	public void startElasticSearchClient() {
		try {
			Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch").build();
			client = new TransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(esClientHost, esClientPort));
		} catch (Exception e) {
			throw new RuntimeException("Could not start ES client : " + e.getMessage());
		}
	}


	@Override
	public void initialize() throws Exception {
		startEmbeddedElasticSearch();
		startElasticSearchClient();
	}

	@Override
	public void shutDown() {
		cluster.stop();
	}
}
