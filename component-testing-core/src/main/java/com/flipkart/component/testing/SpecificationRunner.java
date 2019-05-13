package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;

import java.io.File;
import java.util.List;

/**
 * Runs a single test specification : Need to extend for multiple
 */
public class SpecificationRunner {
    private static SpecificationRunner specificationRunner;
    private HttpTestOrchestrator httpTestOrchestrator;

    private ObjectMapper objectMapper = new ObjectMapper();
//    private StormTestOrchestrator stormTestOrchestrator;


    /**
     * For Dropwizard based services
     * @param serviceConfigPath
     * @param serviceUrl
     * @param serviceClass
     */
    private SpecificationRunner(String serviceConfigPath, String serviceUrl, Class<?> serviceClass) {
        SUT sut = new DropwizardServiceStarter(serviceConfigPath, serviceUrl, serviceClass);
        httpTestOrchestrator = new HttpTestOrchestrator(sut);
    }

    public static SpecificationRunner getInstance(String serviceConfigPath, String serviceUrl, Class<?> serviceClass){
        if(specificationRunner == null){
            specificationRunner = new SpecificationRunner(serviceConfigPath,serviceUrl,serviceClass);
        }
        return specificationRunner;
    }

    public SpecificationRunner(SUT sut){
        httpTestOrchestrator = new HttpTestOrchestrator(sut);
    }

    /**
     * runs a single test
     *
     * @param specFilePath
     * @return
     * @throws Exception
     */
    public List<Observation> run(String specFilePath) throws Exception {
        TestSpecification testSpecification = objectMapper.readValue(new File(specFilePath), TestSpecification.class);
        return httpTestOrchestrator.run(testSpecification);
    }


    /**
     * runs a test in a light weight fashion. Only clean up operation will be called and shutdown needs to be called by the test writer.
     *
     * @param specFilePath
     * @return
     */
    public List<Observation> runLite(String specFilePath) throws Exception {
        TestSpecification testSpecification = objectMapper.readValue(new File(specFilePath), TestSpecification.class);
        return runLite(testSpecification);
    }

    public List<Observation> runLite(TestSpecification testSpecification) throws Exception{
        return httpTestOrchestrator.runLite(testSpecification);
    }

    /**
     * shutsdown all the dependencies that were initialized until now: This should be called in the end the of tests run => end of test suite run
     */
    public void shutDown() {
        try {
            CompositeDependencyRegistry.getInstance().shutDown();
        } catch (Exception e) {
            System.out.println("Error in shutting down all dependencies : You may face problems in next run");
        }
    }


    /**
     * runLite for a storm topology
     * @param specFile
     * @param tuplesToBeEmitted
     */
//    public List<Observation> runLite(String specFile, int tuplesToBeEmitted) throws Exception {
//        TestSpecification testSpecification = objectMapper.readValue(new File(specFile), TestSpecification.class);
//        return runLite(testSpecification, tuplesToBeEmitted);
//    }

//    public List<Observation> runLite(TestSpecification testSpecification, int tuplesToBeEmitted) throws Exception {
//        return this.stormTestOrchestrator.executeLite(testSpecification, tuplesToBeEmitted);
//    }

}
