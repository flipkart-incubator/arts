package com.flipkart.component.testing.model.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * a base class for both http Direct Input and http Indirect Input
 */
abstract class HttpInput{

    /**
     * http api path
     */
    @Getter
    private String path;

    /**
     * http method type
     */
    private METHOD method;


    @JsonCreator
    HttpInput(@JsonProperty("path") String path, @JsonProperty("method") METHOD method){
        this.path = path;
        this.method = method;
    }


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

}
