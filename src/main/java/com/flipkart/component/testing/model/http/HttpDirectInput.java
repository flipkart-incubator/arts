package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.DirectInput;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@JsonTypeName("httpDirectInput")
@Getter
@ToString
public class HttpDirectInput extends HttpInput implements DirectInput {

    private final Object request;
    private final Map<String, String> headers;


    @JsonCreator
    public HttpDirectInput(@JsonProperty("path") String path,
                           @JsonProperty("method") METHOD method,
                           @JsonProperty("json") Object request,
                           @JsonProperty("headers") Map<String,String> headers){
        super(path, method);
        this.request = request;
        this.headers = headers;
    }

}
