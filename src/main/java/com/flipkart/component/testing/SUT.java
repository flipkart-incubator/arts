package com.flipkart.component.testing;

/**
 * SUT = System Under Test
 * Hook needs to be provided by the test writer on how to start a service
 */
@FunctionalInterface
public interface SUT {

    /**
     * the logic to start a service should be embedded here: Default setting to empty
     */
    default void start(){};

    /**
     * the endpoint of a service along with port should be included here. for example  http://localhost:25666
     * @return return the url of System under test : Assuming http for now
     */
    String getUrl();
}
