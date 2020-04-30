package com.flipkart.component.testing;

import com.flipkart.component.testing.model.elasticsearch.v2.ElasticSearchV2Config;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.Arrays;

class RemoteElasticSearchV2Operations implements ElasticSearchV2Operations {

    private static RemoteElasticSearchV2Operations instance;
    private RestHighLevelClient restHighLevelClient;

    private RemoteElasticSearchV2Operations(ElasticSearchV2Config elasticSearchTestConfig) {
        try {
            HttpHost httpHost = new HttpHost(elasticSearchTestConfig.getHost(), elasticSearchTestConfig.getPort(), "http");

            RestClientBuilder builder = RestClient.builder(httpHost)
                    .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                            .setDefaultCredentialsProvider(getCredentialsProvider(elasticSearchTestConfig.getUsername(),
                                     elasticSearchTestConfig.getPassword())));
            this.restHighLevelClient = new RestHighLevelClient(builder);
        } catch (Exception e) {
            throw new RuntimeException("Could not start ES client : " + e.getMessage());
        }
    }

    @Override
    public RestHighLevelClient getClient() {
        return this.restHighLevelClient;
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
        try {
            this.restHighLevelClient.indices().refresh(new RefreshRequest(indices), RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Could not refresh indices " +
                    Arrays.toString(indices) + " due to " + e.getMessage());
        }
    }

    @Override
    public void deleteIndex(String index) {
        throw new UnsupportedOperationException("safe side not deleting remote index");
    }

    @Override
    public boolean isIndexPresent(String index) {
        try {
            return this.restHighLevelClient.indices()
                    .exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void createIndex(String index, String type, String mappingFileContent) {
        try {
            this.restHighLevelClient.indices().create(new CreateIndexRequest(index).mapping(mappingFileContent, XContentType.JSON), RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Could not create index " +
                    index + " due to " + e.getMessage());
        }
    }

    /**
     * All indices with prefix 'regression_' will be deleted after running this operation.
     */
    @Override
    public void deleteIndices() {
        try {
            GetIndexResponse getIndexResponse = restHighLevelClient.indices()
                    .get(new GetIndexRequest("regression_*"), RequestOptions.DEFAULT);
            if (getIndexResponse.getIndices() != null && getIndexResponse.getIndices().length != 0)
                restHighLevelClient.indices()
                        .delete(new DeleteIndexRequest(getIndexResponse.getIndices()), RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete all indices with prefix regression_ due to " + e.getMessage());
        }
    }

    private CredentialsProvider getCredentialsProvider(String username, String password) {
        CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
        return credentialsProvider;
    }

    public static ElasticSearchV2Operations getInstance(ElasticSearchV2Config elasticSearchTestConfig) {
        if (instance == null) instance = new RemoteElasticSearchV2Operations(elasticSearchTestConfig);
        return instance;
    }
}
