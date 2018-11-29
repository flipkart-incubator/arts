package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;

public class DependencyInitializerImplTest {

    @Test
    public void test() throws Exception {

        List<IndirectInput> indirectInputList = new ArrayList<>();
        indirectInputList.add(mock(HttpIndirectInput.class));
        indirectInputList.add(mock(HttpIndirectInput.class));

        List<Observation> observations = new ArrayList<>();
        observations.add(new HttpObservation(200, new HashMap(), new HashMap<>()));

        TestData testData = new TestData(null, indirectInputList, observations);
        DependencyInitializer dependencyInitializer = new DependencyInitializerImpl(testData);

        // each initializer should be initlaized only once
        // if the same server is getting initialized multiple times then this will fail due to address already in use
        dependencyInitializer.initialize();
    }
}
