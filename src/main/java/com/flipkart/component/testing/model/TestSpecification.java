package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Definition to define the test data for a test case
 * Influenced from xunit Patterns : http://xunitpatterns.com/SUT.html
 */
@Getter
@ToString
public class TestSpecification {

    private final DirectInput directInput;
    private final List<IndirectInput> indirectInputs;
    private final List<Observation> observations;

    @Setter
    private String description;

    public TestSpecification(@JsonProperty("directInput") DirectInput directInput,
                             @JsonProperty("indirectInputs") List<IndirectInput> indirectInputs,
                             @JsonProperty("observations") List<Observation> observations){
        this.directInput = directInput;
        this.indirectInputs = indirectInputs;
        this.observations = observations;
    }
}
