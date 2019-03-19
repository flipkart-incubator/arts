package com.flipkart.component.testing.model.aerospike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@Data
public class AerospikeData{

    private String namespace;
    private String set;
    private List<AerospikeRecords> records;

    @JsonCreator
    public AerospikeData(
            @JsonProperty("namespace") String namespace,
            @JsonProperty("set") String set,
            @JsonProperty("records") List<AerospikeRecords> records)
    {

        this.namespace = namespace;
        this.set = set;
        this.records = records;
    }

    public String getNamespace() {
        if (!namespace.contains("regression_")){
            throw new RuntimeException("The namespace name used is not in right format for testing. Try prefix regression_");
        }
        else return namespace;

    }


    @Data
    public static class AerospikeRecords {
        @Getter
        private String primaryKey;
        private Map<String,Object> binData;

        @JsonCreator
        public AerospikeRecords(@JsonProperty("PK") String primaryKey,
                                @JsonProperty("binData") Map<String, Object> binData){
            this.primaryKey= primaryKey;
            this.binData= binData;
        }

    }
}

