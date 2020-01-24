package com.flipkart.component.testing.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

@Data
public class Document{

    @Getter
    private String documentId;
    @Getter
    private String parentId;
    @Getter
    private Map<String,Object> data;

    @JsonCreator
    public Document(@JsonProperty("_id")String documentId,
                    @JsonProperty("_parentId") String parentId,
                    @JsonProperty("data") Map<String,Object> data){
        this.documentId = documentId;
        this.parentId = parentId;
        this.data = data;
    }

}
