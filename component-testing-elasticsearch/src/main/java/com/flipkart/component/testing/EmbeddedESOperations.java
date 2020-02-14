package com.flipkart.component.testing;

import com.flipkart.component.testing.model.elasticsearch.ElasticSearchTestConfig;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EmbeddedESOperations implements ElasticSearchOperations {

    private TransportClient transportClient;

    private static EmbeddedESOperations instance;
    private EmbeddedElastic cluster;
    ElasticSearchTestConfig elasticSearchTestConfig;

    private EmbeddedESOperations(ElasticSearchTestConfig elasticSearchTestConfig) {
        try {
            this.elasticSearchTestConfig = elasticSearchTestConfig;
            Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", elasticSearchTestConfig.getClusterName()).build();
            this.transportClient = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(elasticSearchTestConfig.getHost(), ESFactory.ES_CLIENT_PORT));
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
            String downloadURL = elasticSearchTestConfig.getClusterDownloadURL();
            if(downloadURL!= null) {
                String urlProtocol = new URL(downloadURL).getProtocol();
                // the zip file must be named in the format name-version.zip
                if (!Pattern.compile("-([^/]*).zip").matcher(downloadURL).find())
                    throw new IllegalArgumentException("Zip file must be named in the format: \"name-version.zip\". For eg : elasticsearch-2.3.0.zip ");

                else if (new File(new URL(downloadURL).getPath()).isFile() || urlProtocol.equals("https") || urlProtocol.equals("http") )
                    this.cluster = EmbeddedElastic.builder()
                            .withSetting(PopularProperties.HTTP_PORT, ESFactory.ES_CLUSTER_PORT)
                            .withDownloadUrl(new URL(downloadURL))
                            .build();
                else
                    this.cluster = EmbeddedElastic.builder()
                            .withSetting(PopularProperties.HTTP_PORT, ESFactory.ES_CLUSTER_PORT)
                            .withElasticVersion(ESFactory.ES_CLUSTER_VERSION)
                            .build();
            }
            else
                this.cluster = EmbeddedElastic.builder()
                        .withSetting(PopularProperties.HTTP_PORT, ESFactory.ES_CLUSTER_PORT)
                        .withElasticVersion(ESFactory.ES_CLUSTER_VERSION)
                        .build();
            this.cluster.start();
        } catch (Exception e) {
            throw new RuntimeException("Unable to start the IN_MEMORY ES cluster "+e);
        }
    }


    @Override
    public void stopCluster() {
        this.cluster.stop();
    }

    @Override
    public void refresh(String[] indices) {
        this.cluster.refreshIndices();
        this.transportClient.admin().indices().prepareRefresh(indices);
    }

    @Override
    public void deleteIndex(String index) {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest().indices(index);
        this.transportClient.admin().indices().delete(deleteIndexRequest).actionGet();
    }

    @Override
    public boolean isIndexPresent(String index) {
        return this.transportClient.admin().indices()
                .exists(new IndicesExistsRequest().indices(new String[]{index}))
                .actionGet()
                .isExists();

    }

    @Override
    public boolean isTypePresent(String index , String type) {
        return this.transportClient.admin().indices()
                .typesExists(new TypesExistsRequest(new String[]{index} , new String[]{type}))
                .actionGet()
                .isExists();
    }

    @Override
    public void createIndex(String index, String type) {
        this.transportClient.admin().indices().create(new CreateIndexRequest(index)).actionGet();
    }

    @Override
    public void deleteIndices() {
        this.transportClient.admin().indices().prepareDelete("_all").get();
    }

    @Override
    public void executePutMapping(String index, String type, String mapping){
        this.transportClient.admin().indices().preparePutMapping(index).setType(type).setSource(mapping).execute().actionGet();
    }

}
