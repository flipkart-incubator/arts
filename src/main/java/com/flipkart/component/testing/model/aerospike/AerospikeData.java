package com.flipkart.component.testing.model.aerospike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
@EqualsAndHashCode
public class AerospikeData{

    private String namespace;
    private String host;
    private int port;
    private String set;
    private List<Map<String,Map<String,String>>> records;

    @JsonCreator
    public AerospikeData(
            @JsonProperty("namespace") String namespace,
            @JsonProperty("host") String host,
            @JsonProperty("port") int port,
            @JsonProperty("set") String set,
            @JsonProperty("records") List<Map<String,Map<String,String>>> records)
    {

        this.namespace = namespace;
        this.host = host;
        this.port = port;
        this.set = set;
        this.records = records;
    }

    public String getNamespace() {
        return namespace;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public List<Map<String,Map<String,String>>>  getRecords() {
        return records;
    }

    public String getSet() {
        return set;
    }

}

