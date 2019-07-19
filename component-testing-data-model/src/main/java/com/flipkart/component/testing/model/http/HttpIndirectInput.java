package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.TestConfig;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.util.*;

/**
 * Representation for a Http Indirect Input.
 */
@JsonTypeName("httpIndirectInput")
@Getter
@ToString
public class HttpIndirectInput implements IndirectInput, TestConfig {

    private  Map<String, Object> specification;

    private String inputFile;
    public static List<Map<String,Object>> requestList =new ArrayList<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

    @JsonCreator
    public HttpIndirectInput(@JsonProperty("specification") Map<String, Object> specification,
                             @JsonProperty("mockApiFile")String inputFile) {

        this.inputFile=inputFile;
        if (specification == null && inputFile ==null) {
            throw new IllegalArgumentException("Provide at least one specification. Either from spec file or from mockApiFile.");
        } else if (specification!=null){
            Map<String, Object> map = (Map<String, Object>) specification.get("response");
            try {
                String body = objectMapper.writeValueAsString(map.get("body"));
                map.put("body", body);
                requestList.add((Map<String,Object>)specification.get("request"));
                this.specification = specification;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("unable to serialize" + specification);
            }
        }
    }

    /**
     * config for indirect input to whether load before or after SUT start
     *
     * @return
     */
    @Override
    public boolean isLoadAfterSUT() {
        return false;
    }
}
