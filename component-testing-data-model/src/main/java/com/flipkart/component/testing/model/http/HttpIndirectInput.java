package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.TestConfig;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;

/**
 * Representation for a Http Indirect Input.
 */
@JsonTypeName("httpIndirectInput")
@Getter
@ToString
@Slf4j
public class HttpIndirectInput implements IndirectInput, TestConfig {

    private  Map<String, Object> specification;

    private String inputFile;
    public static List<Map<String,Object>> requestList =new ArrayList<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

    public HttpIndirectInput(Map<String, Object> specification){
        this(specification,null);
    }

    @JsonCreator
    public HttpIndirectInput(@JsonProperty("specification") Map<String, Object> specification,
                             @JsonProperty("mockApiFile")String inputFile) {
        this.inputFile=inputFile;
        this.specification = addSpecification(specification,inputFile);
    }

    private Map<String, Object> addSpecification(Map<String, Object> specification,String inputFile) {
        if (specification == null && inputFile ==null) {
            throw new IllegalArgumentException("Provide at least one specification. Either from spec file or from mockApiFile.");
        } else if (specification!=null){
            Map<String, Object> map = (Map<String, Object>) specification.get("response");
            try {
                String body = objectMapper.writeValueAsString(map.get("body"));
                map.put("body", body);
                requestList.add((Map<String,Object>)specification.get("request"));
            } catch (JsonProcessingException e) {
                log.error("unable to serialize  : {} ", specification, e);
                throw new RuntimeException("unable to serialize" + specification);
            }
        }
        return specification;
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
