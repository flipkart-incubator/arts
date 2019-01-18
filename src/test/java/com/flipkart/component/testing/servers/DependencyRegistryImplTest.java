package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.model.*;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.http.HttpObservation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.mock;

public class DependencyRegistryImplTest {

    @Test
    public void test() throws Exception {

        List<IndirectInput> indirectInputList = new ArrayList<>();
        indirectInputList.add(mock(HttpIndirectInput.class));
        indirectInputList.add(mock(HttpIndirectInput.class));

        List<Observation> observations = new ArrayList<>();
        observations.add(new HttpObservation(200, new HashMap(), new HashMap<>()));

        TestSpecification testSpecification = new TestSpecification(null, null, indirectInputList, observations);
        // each initializer should be initlaized only once
        // if the same server is getting initialized multiple times then this will fail due to address already in use
        DependencyRegistry.INSTANCE.registerAndInitialize(testSpecification);
        DependencyRegistry.INSTANCE.registerAndInitialize(testSpecification);


    }
}
