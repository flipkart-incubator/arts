package com.flipkart.component.testing.model.hazelcast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.shared.HazelcastTestConfig;
import lombok.Getter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Map;

/**
 * @author siddharth.t
 */

@Getter
@JsonTypeName("serverhzIndirectInput")
@JsonIgnoreProperties
public class ServerHZIndirectInput implements HazelcastIndirectInput, HazelcastTestConfig {

    private final String groupName;

    private final String password;

    private final HazelcastDataStructures hazelcastDS;

    private final Map<String ,String> serializerConfigMap;

    @JsonCreator
    public ServerHZIndirectInput(@JsonProperty("groupName") String groupName,
                                 @JsonProperty("password") String password,
                                 @JsonProperty("hazelcastDS") HazelcastDataStructures hazelcastDataStructures,
                                 @JsonProperty("serializerConfigMap") Map<String, String> serializerConfigs) {
        this.groupName = groupName;
        this.password = password;
        this.hazelcastDS = hazelcastDataStructures;
        this.serializerConfigMap = serializerConfigs;
    }

    @Override
    public Map<String, HazelcastMap> getMaps() {
        return hazelcastDS.getMaps();
    }

}
