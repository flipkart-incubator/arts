package com.flipkart.component.testing.model.zookeeper;

import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.shared.ZookeeperOperations;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

import java.util.List;
import java.util.Map;
import java.util.Set;

@JsonTypeName("zookeeperObservation")
@ToString
@EqualsAndHashCode
public class ZookeeperObservation implements Observation {

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
