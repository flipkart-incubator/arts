package com.flipkart.component.testing.model.redis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.shared.RedisTestConfig;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@JsonTypeName("RedisObservation")
@Getter
public class RedisObservation implements Observation, RedisTestConfig {

    private final List<RedisHashMap> hashMaps;

    private final Map<String,String> keyValues;

    @Getter(AccessLevel.PRIVATE)
    private final ClusterType clusterType;

    private final String masterName;


    @JsonCreator
    public RedisObservation(@JsonProperty("rows") List<RedisHashMap> hashMaps,
                            @JsonProperty("clusterType") ClusterType clusterType,
                            @JsonProperty("masterName") String masterName,
                            @JsonProperty("keyValues") Map<String,String> keyValues) {

        this.hashMaps = hashMaps;
        this.clusterType = clusterType;
        this.masterName = masterName;
        this.keyValues = keyValues;
    }

    public RedisObservation(RedisObservation expectedObservation, List<RedisHashMap> hashMaps, Map<String, String> keyValues) {
        this.hashMaps = hashMaps;
        this.keyValues = keyValues;
        this.masterName = expectedObservation.masterName;
        this.clusterType = expectedObservation.clusterType;
    }

    @JsonIgnore
    public boolean isSentinel() {
        return this.clusterType == ClusterType.SENTINEL;
    }
}
