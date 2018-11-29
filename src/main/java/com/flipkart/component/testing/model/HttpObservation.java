package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;

@JsonTypeName("httpObservation")
@Getter
@ToString
public class HttpObservation implements Observation {

    private final int statuscode;
    private final Object response;
    private final Object headers;

    @JsonCreator
    public HttpObservation(@JsonProperty("statuscode") int statuscode, @JsonProperty("response") Object response, @JsonProperty("headers") Object headers){
        this.statuscode = statuscode;
        this.response = response;
        this.headers = headers;
    }
}
