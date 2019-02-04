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
    public EmbeddedHZIndirectInput(@JsonProperty("hazelcastDS") HazelcastDataStructures hazelcastDataStructures){
        this.hazelcastDS = hazelcastDataStructures;
    }

    @Override
    public Map<String, HazelcastMap> getMaps() {
        return hazelcastDS.getMaps();
    }

    @Override
    public Map<String, String> getSerializerConfigMap() {
        throw new UnsupportedOperationException("serializer config cannot be accessed in embedded mode");
    }

    @Override
    public String getGroup() {
        throw new UnsupportedOperationException("group name cannot be accessed in embedded mode");
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException("password cannot be accessed in embedded mode");
    }

    @Override
    public String getUser() {
        throw new UnsupportedOperationException("user name cannot be accessed in embedded mode");
    }
}
