package com.flipkart.component.testing;

import com.flipkart.component.testing.model.Observation;
import org.reflections.Reflections;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Holds the responsibility of collecting Observations: Generally after the test case execution from multiple Data Stores
 */
class CompositeObservationCollector implements ObservationCollector {

    private static CompositeObservationCollector instance;
    private Map<Class, ObservationCollector> map;


    private CompositeObservationCollector(Map<Class, ObservationCollector> map){
        this.map = map;
    }

    static CompositeObservationCollector getInstance() {
        try {
            if (instance == null) {
                instance = new CompositeObservationCollector(register());
            }
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("unable to create observation collectors", e);
        }

        return instance;
    }

    /**
     * registers all available observation collectors in package
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static Map<Class,ObservationCollector> register() throws IllegalAccessException, InstantiationException {

        // get all the classes which implements ObservationCollecytor
        Reflections reflections = new Reflections("com.flipkart.component.testing");
        Set<Class<? extends ObservationCollector>> observationCollectors = reflections.getSubTypesOf(ObservationCollector.class);

        // populate them in a map with key of data-model observation class and value concrete observation collector.
        // Each concrete class should have a default constructor.

        Map<Class, ObservationCollector> map = new HashMap<>();
        for(Class clazz : observationCollectors){
            if(!clazz.getName().equals(CompositeObservationCollector.class.getName())){
                ObservationCollector observationCollector = (ObservationCollector) clazz.newInstance();
                map.put(observationCollector.getObservationClass(), observationCollector);
            }
        }
        return map;
    }

    /**
     * extracts the actual Observation using attributes of expected observation
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public Observation actualObservations(Observation expectedObservation) throws IOException {
        ObservationCollector observationCollector = map.get(expectedObservation.getClass());
        if(observationCollector == null){
            throw new IllegalArgumentException("observation's dependency is not registered. Please check whether maven dependency is added or not for " + expectedObservation.getClass());
        }
        return observationCollector.actualObservations(expectedObservation);
    }

    @Override
    public Class getObservationClass() {
        throw new UnsupportedOperationException();
    }

}
