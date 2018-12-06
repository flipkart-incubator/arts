package com.flipkart.component.testing.model.aerospike;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@JsonTypeName("AerospikeObservation")
@Getter
@EqualsAndHashCode
public class AerospikeObservation implements Observation {

    private final AerospikeData data;

    public AerospikeData getData() {
        return data;
    }

    @JsonCreator
    public AerospikeObservation(@JsonProperty("data") AerospikeData data){
        this.data = data;
    }
}
