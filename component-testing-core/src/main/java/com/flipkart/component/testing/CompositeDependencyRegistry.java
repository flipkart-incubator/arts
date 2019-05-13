package com.flipkart.component.testing;

import com.flipkart.component.testing.model.TestConfig;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.hazelcast.HazelcastObservation;
import com.flipkart.component.testing.model.hbase.HBaseObservation;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CompositeDependencyRegistry implements DependencyRegistry{

    private static CompositeDependencyRegistry instance;
    private final Map<Class, DependencyInitializer> map;
    private final Set<DependencyInitializer> dependencyInitializers;

    private CompositeDependencyRegistry(Map<Class, DependencyInitializer> map) {
        this.map = map;
        this.dependencyInitializers = new HashSet<>();
    }

    @Override
    public void registerDependencies(TestSpecification testSpecification) {
        // initialize a dependency if not already initialized
        testSpecification.getIndirectInputs().forEach( ii -> registerDependency(map.get(ii.getClass()), ii));
        testSpecification.getObservations().forEach( ii -> registerDependency(map.get(ii.getClass()), ii));
    }

    @SuppressWarnings("unchecked")
    private void registerDependency(DependencyInitializer dependencyInitializer, TestConfig testConfig){
        if(!dependencyInitializers.contains(dependencyInitializer)){
            try {
                if(dependencyInitializer == null || testConfig instanceof HazelcastObservation){ // TODO:PAVAN hack for check of observation
                    System.out.println("WARNING: Dependency Initializer is not found for" + testConfig.getClass().getName());
                    return;
                }
                dependencyInitializers.add(dependencyInitializer);
                dependencyInitializer.initialize(testConfig);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static CompositeDependencyRegistry getInstance(){
        if(instance == null){
            try {
                instance = new CompositeDependencyRegistry(register());
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("unable to register", e);
            }
        }
        return instance;
    }

    private static Map<Class, DependencyInitializer> register() throws IllegalAccessException, InstantiationException {
        Reflections reflections = new Reflections("com.flipkart.component.testing");
        Set<Class<? extends DependencyInitializer>> dependencyInitializers = reflections.getSubTypesOf(DependencyInitializer.class);

        Map<Class, DependencyInitializer> map = new HashMap<>();
        for(Class clazz : dependencyInitializers){
            if(!clazz.getName().equals(DependencyRegistry.class.getName())
                    && !clazz.getName().equals(CompositeDependencyRegistry.class.getName())){
                DependencyInitializer dependencyInitializer = (DependencyInitializer) clazz.newInstance();
                map.put(dependencyInitializer.getObservationClass(), dependencyInitializer);
                map.put(dependencyInitializer.getIndirectInputClass(), dependencyInitializer);
            }
        }
        return map;
    }

    @Override
    public void initialize(TestConfig testConfig) throws Exception {
        // initialized as part of register
    }

    @Override
    public void shutDown() {
        dependencyInitializers.forEach(DependencyInitializer::shutDown);
        dependencyInitializers.clear();
    }

    @Override
    public void clean() {
        dependencyInitializers.forEach(DependencyInitializer::clean);
    }

    @Override
    public Class getIndirectInputClass() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class getObservationClass() {
        throw new UnsupportedOperationException();
    }

}
