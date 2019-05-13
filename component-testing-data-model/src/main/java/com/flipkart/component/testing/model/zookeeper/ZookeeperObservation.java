package com.flipkart.component.testing.model.zookeeper;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestConfig;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

@JsonTypeName("zookeeperObservation")
@ToString
@EqualsAndHashCode
public class ZookeeperObservation implements Observation,TestConfig {

    @Getter
    private final Set<String> paths;

    @Getter
    private final Set<String> valuePaths;

    private Set<String> exists;
    private Map<String, byte[]> values;

    public boolean exists(String path) {
        return this.exists.contains(path);
    }

    public byte[] getValue(String path) {
        return this.values.get(path);
    }

    @JsonCreator
    public ZookeeperObservation(@JsonProperty("paths") Set<String> paths, @JsonProperty("valuePaths") Set<String> valuePaths) {
        this.paths = paths;
        this.valuePaths = valuePaths;
    }

    public ZookeeperObservation(Set<String> exists, Map<String, byte[]> values) {
        this.exists = exists;
        this.values = values;
        this.paths = null;
        this.valuePaths = null;
    }
}
