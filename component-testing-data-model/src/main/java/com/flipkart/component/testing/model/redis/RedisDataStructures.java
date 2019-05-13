package com.flipkart.component.testing.model.redis;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class RedisDataStructures {

    /**
     * first key is redis level key : to identify hashmap
     * second set of keys are within hashmap
     */
    private Map<String, Map<String, String>> hashMap;

    private Map<String, String> keyValues;

    private Map<String, Set<String>> set;

    private Map<String, Map<String,Double>> sortedSets;

    private Map<String, List<String>> lists;

    public RedisDataStructures(){
        this.hashMap = new HashMap<>();
        this.keyValues = new HashMap<>();
        this.set = new HashMap<>();
        this.sortedSets = new HashMap<>();
        this.lists = new HashMap<>();
    }

    public void addHashMap(Map<String,Map<String, String>> hashMap) {
        this.hashMap = hashMap;
    }

    public void addKeyValues(Map<String, String> map) {
        this.keyValues = map;
    }

    public void addSet(Map<String, Set<String>> set){
        this.set = set;
    }

    public void addSortedSet(Map<String, Map<String, Double>> sortedSets) {
        this.sortedSets = sortedSets;
    }
    public void addLists(Map<String, List<String>> lists) {
        this.lists = lists;
    }
}
