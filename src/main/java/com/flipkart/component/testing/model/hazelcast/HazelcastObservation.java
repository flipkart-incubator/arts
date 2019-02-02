package com.flipkart.component.testing.model.hazelcast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author siddharth.t
 */
@JsonTypeName("hazelcastObservation")
@Getter
public class HazelcastObservation implements Observation {

    @Setter
    private HazelcastDataStructures hazelcastDS;

    private final Map<String, List<String>> dStoFetch;


    @JsonCreator
    public HazelcastObservation(@JsonProperty("dsToFetch") Map<String, List<String>> DSToFetch){
        this.dStoFetch = DSToFetch;
    }

}
