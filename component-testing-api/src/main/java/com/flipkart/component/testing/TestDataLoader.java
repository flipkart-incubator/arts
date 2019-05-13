package com.flipkart.component.testing;

import com.flipkart.component.testing.model.IndirectInput;

import java.util.List;

/**
 * Definition to load indirectInput into relevant components.
 * Method parameters are indirectInputs based on the assumption that for any test case only indirect inputs needs to be loaded
 */
public interface TestDataLoader<T extends IndirectInput> {

    /**
     * loads the indirectinput into relevant components
     *
     * @param indirectInput
     */
    void load(T indirectInput);

    /**
     * A utility method for load multiple data sets
     *
     * @param indirectInputs
     */
    default void load(List<T> indirectInputs) {
        indirectInputs.forEach(this::load);
    }


    /**
     * returns the indirectInput class associated with this loader.
     */
    List<Class<T>> getIndirectInputClasses();

}
