package com.flipkart.component.testing.model.zookeeper;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.TestConfig;
import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@JsonTypeName("zookeeeperIndirectInput")
@Getter
public class ZookeeperIndirectInput implements IndirectInput,TestConfig {

    private Set<String> pathsToCreate;

    private Map<String, String> dataToLoad;


    @JsonCreator
    public ZookeeperIndirectInput(@JsonProperty("pathsToCreate") Set<String> pathsToCreate, @JsonProperty("dataToLoad") Map<String,String> dataToLoad){
        this.pathsToCreate = new TreeSet<>(pathsToCreate);
        this.dataToLoad = dataToLoad;
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
