package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.internal.Constants;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

public class EmbeddedESOperations implements ElasticSearchOperations {

    private TransportClient transportClient;

    private static EmbeddedESOperations instance;
    private EmbeddedElastic cluster;

    private EmbeddedESOperations(ElasticSearchTestConfig elasticSearchTestConfig) {
        try {
            Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", elasticSearchTestConfig.getClusterName()).build();
            this.transportClient = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(elasticSearchTestConfig.getHost(), Constants.ES_CLIENT_PORT));
        } catch (Exception e) {
            throw new RuntimeException("Could not start ES client : " + e.getMessage());
        }
    }

    public static EmbeddedESOperations getInstance(ElasticSearchTestConfig elasticSearchTestConfig) {
        if (instance == null) instance = new EmbeddedESOperations(elasticSearchTestConfig);
        return instance;
    }

    @Override
    public Client getClient() {
        return this.transportClient;
    }

    @Override
    public void startCluster() {
        try {
            this.cluster = EmbeddedElastic.builder()
                    .withSetting(PopularProperties.HTTP_PORT, Constants.ES_CLUSTER_PORT)
                    .withElasticVersion(Constants.ES_CLUSTER_VERSION)
                    .build();
            this.cluster.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void stopCluster(){
        this.cluster.stop();
    }

    @Override
    public void refresh() {
        this.cluster.refreshIndices();
    }
}
