package com.flipkart.component.testing.shared;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.solr.core.CoreContainer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class EmbeddedSolrOperations implements SolrOperations {
    private static EmbeddedSolrOperations embeddedSolrOperations;
    EmbeddedSolrServer embeddedSolrServer;
    private File testDatadir;
    SolrTestConfig solrTestConfig;

    private EmbeddedSolrOperations(SolrTestConfig solrTestConfig) {
        try {
            this.solrTestConfig=solrTestConfig;
            createTestdataDir();
            CoreContainer container = new CoreContainer(testDatadir.getAbsolutePath());
            container.load();
            embeddedSolrServer = new EmbeddedSolrServer(container, solrTestConfig.getSolrCoreName());
            createCore();
        } catch (Exception e) {
            throw new RuntimeException("Could not start Solr embedded server : " + e.getMessage());
        }
    }
    public static EmbeddedSolrOperations getEmbeddedSolrInstance(SolrTestConfig solrTestConfig) {
        if (embeddedSolrOperations== null)
            embeddedSolrOperations=  new EmbeddedSolrOperations(solrTestConfig);
        return embeddedSolrOperations;
    }


    @Override
    public void startServer() {
    }


    @Override
    public void stopServer() {
        try {
            embeddedSolrServer.close();
            deleteTestDataDir();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void createCore() {
        try {
            new CoreAdminRequest().createCore(solrTestConfig.getSolrCoreName(), testDatadir.getAbsolutePath(), embeddedSolrServer);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void commit() {
        try {
            embeddedSolrServer.commit();
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clean() {
        try {
            embeddedSolrServer.deleteByQuery("*:*");
            commit();
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void addSolrDocument(SolrInputDocument solrDocument) {
        try {
            embeddedSolrServer.add(solrDocument);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public QueryResponse getQueryResponse(String queryFilePath) {
        Map<String, String> query= null;
        try {
            query = new ObjectMapper().readValue(new String(Files.readAllBytes(Paths.get(
                    new File(queryFilePath).getAbsolutePath()))), HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            return embeddedSolrServer.query(solrTestConfig.getSolrCoreName(),
                    new MapSolrParams(query));
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

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
        embeddedSolrServer.getCoreContainer().unload(solrTestConfig.getSolrCoreName());
        if(testDatadir!=null) {
            try {
                FileUtils.deleteDirectory(testDatadir);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
