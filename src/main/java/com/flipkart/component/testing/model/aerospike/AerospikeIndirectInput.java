package com.flipkart.component.testing.model.aerospike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import lombok.Getter;


@JsonTypeName("AerospikeIndirectInput")
@Getter
public class AerospikeIndirectInput implements IndirectInput {


    private final AerospikeData data;

    public AerospikeData getData() {
        return data;
    }

    @JsonCreator
    public AerospikeIndirectInput(@JsonProperty("aerospikeData") AerospikeData data){
        this.data = data;
    }
}
