package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Definition to define the test data for a test case
 * Influenced from xunit Patterns : http://xunitpatterns.com/SUT.html
 */
@Getter
@ToString
public class TestSpecification {

    private final Integer ttlInMs;
    private final DirectInput directInput;
    private final List<IndirectInput> indirectInputs;
    private final List<Observation> observations;

    @Setter
    private String description;

    public TestSpecification(@JsonProperty("ttlInMs") Integer ttlInMs,
                             @JsonProperty("directInput") DirectInput directInput,
                             @JsonProperty("indirectInputs") List<IndirectInput> indirectInputs,
                             @JsonProperty("observations") List<Observation> observations){
        this.directInput = directInput;
        this.indirectInputs = indirectInputs;
        this.observations = observations;
        //ttl should lie bwn 10 - 100s, otherwise default to 100s
        this.ttlInMs = (ttlInMs!= null && ttlInMs >= 10000 && ttlInMs <= 100000) ? ttlInMs : 100000;
    }


    @JsonIgnore
    public List<IndirectInput> getIndirectInputsToBePreLoaded(){
        return this.indirectInputs.stream().filter(e -> !e.isLoadAfterSUT()).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<IndirectInput> getIndirectInputsToBePostLoaded(){
        return this.indirectInputs.stream().filter(IndirectInput::isLoadAfterSUT).collect(Collectors.toList());
    }
}
