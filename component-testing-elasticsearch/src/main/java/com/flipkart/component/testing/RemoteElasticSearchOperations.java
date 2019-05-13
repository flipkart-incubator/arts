package com.flipkart.component.testing;

import com.flipkart.component.testing.model.elasticsearch.ElasticSearchTestConfig;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

class RemoteElasticSearchOperations implements ElasticSearchOperations {

    private static RemoteElasticSearchOperations instance;
    private final TransportClient transportClient;

    private RemoteElasticSearchOperations(ElasticSearchTestConfig elasticSearchTestConfig) {
        try {
            Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", elasticSearchTestConfig.getClusterName()).build();
            this.transportClient = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(elasticSearchTestConfig.getHost(), ESFactory.ES_CLIENT_PORT));
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
    public void refresh(String[] indices) {
        this.transportClient.admin().indices().prepareRefresh(indices);
    }

    @Override
    public void deleteIndex(String index) {
        throw new UnsupportedOperationException("safe side not deleting remote index");
    }

    @Override
    public boolean isIndexPresent(String index) {
        IndicesExistsResponse indicesExistsResponse = this.transportClient.admin().indices().exists(new IndicesExistsRequest().indices(new String[]{index})).actionGet();
        return indicesExistsResponse.isExists();
    }

    @Override
    public void createIndex(String index, String type, String mappingFileContent) {
        this.transportClient.admin().indices().create(new CreateIndexRequest(index).mapping(type, mappingFileContent)).actionGet();
    }

    @Override
    public void deleteIndices() {
        throw new UnsupportedOperationException("safe side not deleting remote indices");
    }

    public static ElasticSearchOperations getInstance(ElasticSearchTestConfig elasticSearchTestConfig) {
        if (instance == null) instance = new RemoteElasticSearchOperations(elasticSearchTestConfig);
        return instance;
    }
}
