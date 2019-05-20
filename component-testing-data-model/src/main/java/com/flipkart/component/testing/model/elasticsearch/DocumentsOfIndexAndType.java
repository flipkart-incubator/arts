package com.flipkart.component.testing.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DocumentsOfIndexAndType {
    private String index;
    private String type;
    private String routingKey;
    private String mappingFile;
    private List<Map<String,Object>> documents;

    @JsonCreator
    public DocumentsOfIndexAndType(@JsonProperty("index") String index,
                                   @JsonProperty("type") String type,
                                   @JsonProperty("routingKey")String routingKey,
                                   @JsonProperty("mappingFile") String mappingFile,
                                   @JsonProperty("documents") List<Map<String, Object>> documents) {
        this.index = index;
        this.type = type;
        this.routingKey = routingKey;
        this.mappingFile = mappingFile;
        this.documents = documents;
    }

    public String getIndex(){
        return index;
    }
}
