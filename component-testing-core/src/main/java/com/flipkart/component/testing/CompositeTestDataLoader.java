package com.flipkart.component.testing;


import com.flipkart.component.testing.model.IndirectInput;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of test data loader which loads the IndirectInputs
 */
class CompositeTestDataLoader implements TestDataLoader {

    private static CompositeTestDataLoader instance;
    private Map<Class, TestDataLoader> map = new HashMap<>();

    private CompositeTestDataLoader(Map<Class, TestDataLoader> map){
        this.map = map;
    }

    public static CompositeTestDataLoader getInstance(){
        if(instance == null){
            try {
                instance = new CompositeTestDataLoader(register());
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("unable to register testdata loader", e);
            }
        }
        return instance;
    }

    /**
     * registers all available observation collectors in package
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static Map<Class,TestDataLoader> register() throws IllegalAccessException, InstantiationException {

        // get all the classes which implements ObservationCollecytor
        Reflections reflections = new Reflections("com.flipkart.component.testing");
        Set<Class<? extends TestDataLoader>> observationCollectors = reflections.getSubTypesOf(TestDataLoader.class);

        // populate them in a map with key of data-model observation class and value concrete observation collector.
        // Each concrete class should have a default constructor.

        Map<Class, TestDataLoader> map = new HashMap<>();
        for(Class clazz : observationCollectors){
            if(!clazz.getName().equals(CompositeTestDataLoader.class.getName())){
                TestDataLoader testDataLoader = (TestDataLoader) clazz.newInstance();
                testDataLoader.getIndirectInputClasses().forEach(e -> map.put((Class) e, testDataLoader));
            }
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(IndirectInput indirectInput) {
        if(map.get(indirectInput.getClass()) == null){
            throw new IllegalArgumentException("indirectInput's dependency is not registered. Please check whether maven dependency is added ot not" + indirectInput.getClass());
        }
        map.get(indirectInput.getClass()).load(indirectInput);
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List getIndirectInputClasses() {
        throw new UnsupportedOperationException();
    }

}

