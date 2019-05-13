package com.flipkart.component.testing;

import com.flipkart.component.testing.model.IndirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestConfig;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.http.HttpObservation;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompositeDependencyRegisterTest {

    @Test
    public void shouldNotGetInitializedTwice(){
        List<IndirectInput> indirectInputList = new ArrayList<>();
        indirectInputList.add(new HttpIndirectInput(ImmutableMap.of("response", new HashMap<>())));
        indirectInputList.add(new HttpIndirectInput(ImmutableMap.of("response", new HashMap<>())));

        List<Observation> observations = new ArrayList<>();
        observations.add(new HttpObservation(200, new HashMap(), new HashMap<>()));

        TestSpecification testSpecification = new TestSpecification(null, null, indirectInputList, observations,null);
        // each initializer should be initlaized only once
        // if the same server is getting initialized multiple times then this will fail due to address already in use
        CompositeDependencyRegistry.getInstance().registerDependencies(testSpecification);
        CompositeDependencyRegistry.getInstance().registerDependencies(testSpecification);
    }

    static class DummyServer implements DependencyInitializer<HttpIndirectInput, HttpObservation, TestConfig> {

        static boolean initalized = false;

        @Override
        public void initialize(TestConfig testConfig) throws Exception {
            if(!initalized) {
                initalized = true;
            }else{
                throw new RuntimeException("initialize should not be called multiple times");
            }
        }

        @Override
        public void shutDown() {

        }

        @Override
        public void clean() {

        }

        @Override
        public Class<HttpIndirectInput> getIndirectInputClass() {
            return HttpIndirectInput.class;
        }

        @Override
        public Class<HttpObservation> getObservationClass() {
            return HttpObservation.class;
        }
    }
}
