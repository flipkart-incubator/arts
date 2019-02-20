package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.internal.Constants;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import java.io.File;
import java.io.IOException;

public class RemoteSolrOperations implements SolrOperations {
    private static RemoteSolrOperations instance = null;
    private HttpSolrClient solrClient = null;
    private SolrTestConfig solrTestConfig;
    private String urlString;
    private File testDatadir;

    private RemoteSolrOperations(SolrTestConfig solrTestConfig) {
        this.solrTestConfig = solrTestConfig;
        urlString = "http://localhost:"+ Constants.SOLR_SERVER_PORT+"/solr/";
        try {
            solrClient = new HttpSolrClient(urlString);
            createTestdataDir();
            createCore();
            solrClient = new HttpSolrClient(urlString+solrTestConfig.getSolrCoreName());
        }catch (Exception e){
            throw new RuntimeException("Failed to connect to host "+e.getMessage());
        }
    }

    public static SolrOperations getRemoteSolrInstance(SolrTestConfig solrTestConfig) {
        if (instance==null)
            instance= new RemoteSolrOperations(solrTestConfig);

        return instance;
    }

    @Override
    public void startServer() {
    }

    @Override
    public void stopServer() {
        try {
            solrClient.close();
            deleteTestDataDir();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void createCore(){
        try {
            new CoreAdminRequest().createCore(solrTestConfig.getSolrCoreName(), testDatadir.getAbsolutePath(), solrClient,
                    testDatadir.getAbsolutePath() + "/solrconfig.xml",
                    testDatadir.getAbsolutePath() + "/managed-schema");
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void commit() {
        try {
            solrClient.commit();
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clean() {
        try {
            solrClient.deleteByQuery("*:*");
            commit();
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addSolrDocument(SolrInputDocument solrDocument) {
        try {
            solrClient.add(solrDocument);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public QueryResponse getQueryResponse(String queryFilePath) {
        return null;
    }

    private void createTestdataDir() {
        testDatadir = new File("dataDir/solr/" + System.getProperty("file.separator") + "SolrTest-" + System.currentTimeMillis());
        testDatadir.mkdirs();
        try {
            FileUtils.copyDirectory(new File(solrTestConfig.getConfigFilePath()),testDatadir.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deleteTestDataDir(){
        if(testDatadir!=null) {
            try {
                FileUtils.deleteDirectory(testDatadir);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
