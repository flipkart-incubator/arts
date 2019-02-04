package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.util.Map;

import static com.flipkart.component.testing.shared.ObjectFactory.OBJECT_MAPPER;

@JsonTypeName("httpObservation")
@Getter
@ToString
public class HttpObservation implements Observation {

    private int statuscode;
    private Object response;
    private Object headers;

    @JsonCreator
    public HttpObservation(@JsonProperty("statuscode") int statuscode, @JsonProperty("response") Object response, @JsonProperty("headers") Object headers){
        this.statuscode = statuscode;
        this.response = response;
        this.headers = headers;
    }


    public HttpObservation(){

    }

    @JsonIgnore
    public Map getResponseAsMap() throws IOException {
        return OBJECT_MAPPER.readValue(this.getResponse().toString(), Map.class);
    }
}
