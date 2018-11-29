package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.Observation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Interface to collect observations from multiple interactions/datastores
 */
public interface ObservationCollector<T extends Observation> {

    ObservationCollector INSTANCE = new ObservationCollectorImpl();
    /**
     * extracts the actual Observation using attributes of expected observation
     *
     * @param expectedObservation
     * @return
     */
    T actualObservations(T expectedObservation) throws IOException;


    default List<T> actualObservations(List<T> expectedObservations) throws IOException {
        List<T> observations = new ArrayList<>();
        for (T expectedObservation : expectedObservations) {
            observations.add(actualObservations(expectedObservation));
        }
        return observations;
    }
}
