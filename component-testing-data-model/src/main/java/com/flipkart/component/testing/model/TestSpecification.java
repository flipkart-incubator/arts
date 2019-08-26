package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.flipkart.component.testing.model.http.HttpObservation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.*;
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
    private final List<IndirectInput> indirectInputs ;
    private final List<Observation> observations;

    /**
     * clean the data if any loaded as a part of this specification: if false won't clean
     */
    private Boolean shouldClean;

    @Setter
    private String description;

    public TestSpecification(@JsonProperty("ttlInMs") Integer ttlInMs,
                             @JsonProperty("directInput") DirectInput directInput,
                             @JsonProperty("indirectInputs") List<IndirectInput> indirectInputs,
                             @JsonProperty("observations") List<Observation> observations,
                             @JsonProperty("clean") Boolean clean){
        this.directInput = directInput;
        this.indirectInputs = new Fanout().fanOutIndirectInputs(indirectInputs);
        this.observations = observations;
        this.shouldClean = clean == null ? true : clean;
        //ttl should lie bwn 10 - 100s, otherwise default to 100s
        this.ttlInMs = (ttlInMs!= null && ttlInMs >= 10000 && ttlInMs <= 100000) ? ttlInMs : 100000;

    }

    @JsonIgnore
    public List<IndirectInput> getIndirectInputsToBePreLoaded(){
        return this.getIndirectInputs().stream().filter(e -> !e.isLoadAfterSUT()).collect(Collectors.toList());
    }

    @JsonIgnore
    public List<IndirectInput> getIndirectInputsToBePostLoaded(){
        return this.getIndirectInputs().stream().filter(IndirectInput::isLoadAfterSUT).collect(Collectors.toList());
    }


//    public List<IndirectInput> getIndirectInputs(){
//
//        // ensuring the order of initialization between kafka and zookeeper.
//        boolean kafkaPresent = indirectInputs.stream().anyMatch(e -> e instanceof KafkaIndirectInput);
//        boolean zkPresent = indirectInputs.stream().anyMatch(e -> e instanceof ZookeeperIndirectInput);
//
//        if(!kafkaPresent) return indirectInputs;
//
//        if(!zkPresent){
//            indirectInputs.add(new ZookeeperIndirectInput(new HashSet<>(), new HashMap<>()));
//        }
//
//        List<IndirectInput> result = new ArrayList<>();
//
//        // TODO: Can there be multiple kafkaIndirectInputs .. this has to be thought through more diligently not only this case. Not implementing for now :
//        // assuming only one indirectInput of one type will be there
//
//        IndirectInput kafkaIndirectInput = null;
//        IndirectInput zookeeperIndirectInput = null;
//
//
//        for(IndirectInput indirectInput : indirectInputs){
//            if(indirectInput instanceof KafkaIndirectInput){
//                kafkaIndirectInput = indirectInput;
//            }else if (indirectInput instanceof ZookeeperIndirectInput){
//                zookeeperIndirectInput = indirectInput;
//            }else{
//                result.add(indirectInput);
//            }
//        }
//
//        result.add(zookeeperIndirectInput);
//        result.add(kafkaIndirectInput);
//        return result;
//    }

    @JsonIgnore
    public List<Observation> sanitizeObservations(String url) {
        List<Observation> observations = new ArrayList<>();
        for (Observation observation : this.observations) {
            if (observation instanceof HttpObservation) {
                observations.add(new HttpObservation(directInput, url));
            } else {
                observations.add(observation);
            }
        }
        return observations;
    }
}
