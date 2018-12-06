package com.flipkart.component.testing.model.redis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.shared.RedisTestConfig;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@JsonTypeName("RedisIndirectInput")
@Getter
public class RedisIndirectInput implements IndirectInput, RedisTestConfig {

    private final List<RedisHashMap> hashMaps;

    private final Map<String,String> keyValues;

    @Getter(AccessLevel.PACKAGE)
    private final ClusterType clusterType;

    private final String masterName;

    @JsonCreator
    public RedisIndirectInput(@JsonProperty("hashMaps") List<RedisHashMap> hashMaps,
                              @JsonProperty("clusterType") ClusterType clusterType,
                              @JsonProperty("masterName") String masterName,
                              @JsonProperty("keyValues") Map<String,String> keyValues) {
        this.hashMaps = hashMaps;
        this.clusterType = clusterType;
        this.masterName = masterName;
        this.keyValues = keyValues;
    }

    @JsonIgnore
    public boolean isSentinel() {
        return this.clusterType == ClusterType.SENTINEL;
    }

}