package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.DirectInput;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@JsonTypeName("httpDirectInput")
@Getter
@ToString
public class HttpDirectInput implements DirectInput {


    /**
     * http api path
     */
    @Getter
    private String path;

    /**
     * http method type
     */
    private METHOD method;


    /**
     * multi part includes file upload
     */
    private MultiPartInput multiPartInput;

    private Object request;

    private final Map<String, String> headers;

    @JsonIgnore
    public boolean isPost(){
        return this.method == METHOD.POST;
    }

    @JsonIgnore
    public boolean isGet(){
        return this.method == METHOD.GET;
    }

    @JsonIgnore
    public boolean isPut(){
        return this.method == METHOD.PUT;
    }

    @JsonIgnore
    public boolean isDelete(){
        return this.method == METHOD.DELETE;
    }

    @JsonIgnore
    public boolean isMultiPart(){
        return multiPartInput != null;
    }


    @JsonCreator
    public HttpDirectInput(@JsonProperty("path") String path,
                           @JsonProperty("method") METHOD method,
                           @JsonProperty("json") Object request,
                           @JsonProperty("headers") Map<String,String> headers,
                           @JsonProperty("multiPartInput") MultiPartInput multiPartInput){
        this.path = path;
        this.method = method;
        this.request = request;
        this.headers = headers;
        this.multiPartInput = multiPartInput;
    }


    @Getter
    @ToString
    public static class MultiPartInput {

        private String filePath;
        private String fileKeyInMultipart;
        private Map<String,String> others;

        public MultiPartInput(@JsonProperty("filePath") String filePath,
                              @JsonProperty("fileKeyInMultipart") String fileKeyInMultipart,
                              @JsonProperty("others") Map<String, String> others){
            this.fileKeyInMultipart = fileKeyInMultipart;
            this.filePath = filePath;
            this.others = others;
        }

    }

}
