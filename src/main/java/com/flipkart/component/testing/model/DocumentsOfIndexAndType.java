package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DocumentsOfIndexAndType {
    private String suffixOfindex;
    private String type;
    private String routingKey;
    private String mappingFile;
    private List<Map<String,Object>> documents;

    @JsonCreator
    public DocumentsOfIndexAndType(@JsonProperty("suffixOfindex") String suffixOfindex,
                                   @JsonProperty("type") String type,
                                   @JsonProperty("routingKey")String routingKey,
                                   @JsonProperty("mappingFile") String mappingFile,
                                   @JsonProperty("documents") List<Map<String, Object>> documents) {
        this.suffixOfindex = suffixOfindex;
        this.type = type;
        this.routingKey = routingKey;
        this.mappingFile = mappingFile;
        this.documents = documents;
    }

    public String getIndex(){
        return "regression_"+this.suffixOfindex;
    }
}
