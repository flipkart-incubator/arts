package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.ToString;

@JsonTypeName("httpDirectInput")
@Getter
@ToString
public class HttpDirectInput extends HttpInput implements DirectInput {

    private final Object request;

    @JsonCreator
    public HttpDirectInput(@JsonProperty("path") String path,
                           @JsonProperty("method") METHOD method,
                           @JsonProperty("json") Object request){
        super(path, method);
        this.request = request;
    }

}
