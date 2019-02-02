package com.flipkart.component.testing.model.hazelcast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

import java.util.Map;

/**
 * @author siddharth.t
 */

@Getter
@JsonTypeName("serverhzIndirectInput")
@JsonIgnoreProperties
public class ServerHZIndirectInput implements HazelcastIndirectInput {

    private final String user;

    private final String password;

    private final HazelcastDataStructures hazelcastDS;

    private final String group;

    private final Map<String ,String> serializerConfigMap;

    @JsonCreator
    public ServerHZIndirectInput(@JsonProperty("group") String group,
                                 @JsonProperty("password") String password,
                                 @JsonProperty("user") String user,
                                 @JsonProperty("hazelcastDS") HazelcastDataStructures hazelcastDataStructures,
                                 @JsonProperty("serializerConfigMap") Map<String, String> serializerConfigs) {
        this.group = group;
        this.password = password;
        this.user = user;
        this.hazelcastDS = hazelcastDataStructures;
        this.serializerConfigMap = serializerConfigs;
    }

    @Override
    public Map<String, HazelcastMap> getMaps() {
        return hazelcastDS.getMaps();
    }
}
