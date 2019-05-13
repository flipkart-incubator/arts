package com.flipkart.component.testing.model.solr;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Data
public class SolrData {


    @Getter
    List<Map<String,Object>> documents;
    String coreName;
    String solrConfigFiles;
    String uniqueKey;

    @JsonCreator
    SolrData(@JsonProperty("coreName")String coreName,
             @JsonProperty("uniqueKey")String uniqueKey,
             @JsonProperty("solrConfigFiles")String solrConfigFile,
             @JsonProperty("documents")List<Map<String,Object>> documents){

        this.coreName= coreName;
        this.uniqueKey= uniqueKey;
        this.documents= documents;
        this.solrConfigFiles= solrConfigFile;
    }
}
