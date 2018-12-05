package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.Constants;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class RemoteElasticSearchOperations implements ElasticSearchOperations {

    private static RemoteElasticSearchOperations instance;
    private final TransportClient transportClient;

    private RemoteElasticSearchOperations(ElasticSearchTestConfig elasticSearchTestConfig) {
        try {
            Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", elasticSearchTestConfig.getClusterName()).build();
            this.transportClient = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(elasticSearchTestConfig.getHost(), Constants.ES_CLIENT_PORT));
        } catch (Exception e) {
            throw new RuntimeException("Could not start ES client : " + e.getMessage());
        }
    }

    @Override
    public Client getClient() {
        return this.transportClient;
    }

    @Override
    public void startCluster() {
        //no op
    }

    @Override
    public void stopCluster() {
        // no op
    }

    @Override
    public void refresh() {
        // no op
    }

    public static ElasticSearchOperations getInstance(ElasticSearchTestConfig elasticSearchTestConfig) {
        if (instance == null) instance = new RemoteElasticSearchOperations(elasticSearchTestConfig);
        return instance;
    }
}
