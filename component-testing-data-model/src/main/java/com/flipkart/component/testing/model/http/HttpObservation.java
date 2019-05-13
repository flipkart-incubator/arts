package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.DirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestConfig;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.util.Map;

@JsonTypeName("httpObservation")
@Getter
@ToString
public class HttpObservation implements Observation,TestConfig {

    @JsonIgnore
    private String url;

    @JsonIgnore
    private DirectInput directInput;

    private int statuscode;
    private Object response;
    private Object headers;
    private static ObjectMapper objectMapper= new ObjectMapper();

    @JsonCreator
    public HttpObservation(@JsonProperty("statuscode") int statuscode, @JsonProperty("response") Object response, @JsonProperty("headers") Object headers){
        this.statuscode = statuscode;
        this.response = response;
        this.headers = headers;
    }


    public HttpObservation(DirectInput directInput, String url){
        this.directInput = directInput;
        this.url = url;
    }

    @JsonIgnore
    public Map getResponseAsMap() throws IOException {
        return objectMapper.readValue(this.getResponse().toString(), Map.class);
    }

    @JsonIgnore
    public <T> T getResponse(Class<T> clazz) throws IOException {
        return objectMapper.readValue(this.getResponse().toString(), clazz);
    }
}
