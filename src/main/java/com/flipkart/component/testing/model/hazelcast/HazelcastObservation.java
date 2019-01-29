package com.flipkart.component.testing.model.hazelcast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.shared.HazelcastTestConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.serialization.Serializer;
import lombok.Getter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author siddharth.t
 */
@JsonTypeName("hazelcastObservation")
@Getter
public class HazelcastObservation implements Observation {

    private final HazelcastDataStructures hazelcastDS;

    private final Map<String, List<String>> dStoFetch;


    @JsonCreator
    public HazelcastObservation(@JsonProperty("dsToFetch") Map<String, List<String>> DSToFetch,
                                @JsonProperty("hazelcastDS") HazelcastDataStructures hazelcastDS){
        this.dStoFetch = DSToFetch;
        this.hazelcastDS = hazelcastDS;
    }

}
