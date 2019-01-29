package com.flipkart.component.testing.model.hazelcast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.util.Map;

/**
 * @author siddharth.t
 */
@JsonTypeName("embeddedhzIndirectInput")
@Getter
public class EmbeddedHZIndirectInput implements HazelcastIndirectInput {

    private final HazelcastDataStructures hazelcastDS;

    @JsonCreator
    public EmbeddedHZIndirectInput(@JsonProperty("hazelcastDS") HazelcastDataStructures hazelcastDataStructures) {
        this.hazelcastDS = hazelcastDataStructures;
    }

    @Override
    public Map<String, HazelcastMap> getMaps() {
        return hazelcastDS.getMaps();
    }

    @Override
    public Map<String, String> getSerializerConfigMap() {
        return null;
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }
}
