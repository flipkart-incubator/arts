package com.flipkart.component.testing.model.redis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.shared.RedisTestConfig;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonTypeName("redisObservation")
@Getter
public class RedisObservation implements Observation, RedisTestConfig {

    @Getter(AccessLevel.PRIVATE)
    private final ClusterType clusterType;

    private final String masterName;

    private final Set<Integer> dbIndices;

    private Map<Integer, RedisDataStructures> dbToDSMap;

    @JsonCreator
    public RedisObservation(@JsonProperty("clusterType") ClusterType clusterType,
                            @JsonProperty("masterName") String masterName,
                            @JsonProperty("dbIndices") Set<Integer> dbIndices
    ) {
        this.clusterType = clusterType;
        this.masterName = masterName;
        this.dbIndices = dbIndices;
    }

    public RedisObservation(RedisObservation expectedObservation) {
        this.masterName = expectedObservation.masterName;
        this.clusterType = expectedObservation.clusterType;
        this.dbIndices = expectedObservation.getDbIndices();
        this.dbToDSMap = new HashMap<>();
        this.dbIndices.forEach(e -> dbToDSMap.put(e, new RedisDataStructures()));
    }

    @JsonIgnore
    public boolean isSentinel() {
        return this.clusterType == ClusterType.SENTINEL;
    }

    @JsonIgnore
    public void addHashMap(int dbIndex, Map<String, Map<String, String>> hashMap) {
        this.dbToDSMap.get(dbIndex).addHashMap(hashMap);
    }

    @JsonIgnore
    public void addKeyValues(int dbIndex, Map<String, String> map) {
        this.dbToDSMap.get(dbIndex).addKeyValues(map);
    }

    @JsonIgnore
    public void addSet(int dbIndex, Map<String, Set<String>> set) {
        this.dbToDSMap.get(dbIndex).addSet(set);
    }

    public void addSortedSet(Integer dbIndex, Map<String, Map<String, Double>> sortedSet) {
        this.dbToDSMap.get(dbIndex).addSortedSet(sortedSet);
    }
}
