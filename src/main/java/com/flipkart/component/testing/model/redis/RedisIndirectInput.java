package com.flipkart.component.testing.model.redis;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.shared.RedisTestConfig;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

@JsonTypeName("redisIndirectInput")
@Getter
public class RedisIndirectInput implements IndirectInput, RedisTestConfig {

    private final ClusterType clusterType;

    private final String masterName;


    private final Map<Integer, RedisDataStructures> dbToDSMap;

    @JsonCreator
    public RedisIndirectInput(@JsonProperty("clusterType") ClusterType clusterType,
                              @JsonProperty("masterName") String masterName,
                              @JsonProperty("dbToDSMap") Map<String, RedisDataStructures> dbToDSMap
    ) {
        this.clusterType = clusterType;
        this.masterName = masterName;
        this.dbToDSMap = new HashMap<>();
        dbToDSMap.forEach((k,v) -> this.dbToDSMap.put(Integer.parseInt(k), v));
    }

    @JsonIgnore
    public boolean isSentinel() {
        return this.clusterType == ClusterType.SENTINEL;
    }


    @JsonIgnore
    public Set<Integer> getDBIndices() {
        return Collections.unmodifiableSet(dbToDSMap.keySet());
    }

    @JsonIgnore
    public Map<String, Map<String, String>> getHashMaps(int dbIndex) {
        return Collections.unmodifiableMap(dbToDSMap.get(dbIndex).getHashMap());
    }

    @JsonIgnore
    public Map<String, String> getKeyValues(int dbIndex) {
        return Collections.unmodifiableMap(dbToDSMap.get(dbIndex).getKeyValues());
    }

    @JsonIgnore
    public Map<String, Set<String>> getSets(int dbIndex){
        return Collections.unmodifiableMap(dbToDSMap.get(dbIndex).getSet());
    }

    @JsonIgnore
    public Map<String, Map<String, Double>> getSortedSets(int dbIndex){
        return Collections.unmodifiableMap(dbToDSMap.get(dbIndex).getSortedSets());
    }

    @JsonIgnore
    public Map<String, List<String>> getLists(int dbIndex){
        return Collections.unmodifiableMap(dbToDSMap.get(dbIndex).getLists());
    }

    /**
     * config for indirect input to whether load before or after SUT start
     *
     * @return
     */
    @Override
    public boolean isLoadAfterSUT() {
        return false;
    }
}