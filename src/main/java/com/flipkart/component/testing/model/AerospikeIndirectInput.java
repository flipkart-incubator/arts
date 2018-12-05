package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;


@JsonTypeName("AerospikeIndirectInput")
@Getter
public class AerospikeIndirectInput implements IndirectInput{


    private final AerospikeData data;

    public AerospikeData getData() {
        return data;
    }

    @JsonCreator
    public AerospikeIndirectInput(@JsonProperty("data") AerospikeData data){
        this.data = data;
    }
}
