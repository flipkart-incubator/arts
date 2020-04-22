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

    /**
     * For Dropwizard based services only
     * @param serviceConfigPath
     * @param serviceUrl
     * @param serviceClass
     */
    private SpecificationRunner(String serviceConfigPath, String serviceUrl, Class<?> serviceClass) {
        SUT sut = new ServiceStarter(serviceConfigPath, serviceUrl, serviceClass);
        httpTestOrchestrator = new HttpTestOrchestrator(sut);
    }

    /**
     * For any given service type
     * @param serviceConfigPath
     * @param serviceUrl
     * @param serviceClass
     * @param serviceType
     */
    private SpecificationRunner(String serviceConfigPath, String serviceUrl, Class<?> serviceClass, Service serviceType) {
        SUT sut = new ServiceStarter(serviceConfigPath, serviceUrl, serviceClass, serviceType);
        httpTestOrchestrator = new HttpTestOrchestrator(sut);
    }


    /**
     * For Dropwizard based services only
     * @param serviceConfigPath
     * @param serviceUrl
     * @param serviceClass
     */
    public static SpecificationRunner getInstance(String serviceConfigPath, String serviceUrl, Class<?> serviceClass){
        if(specificationRunner == null){
            specificationRunner = new SpecificationRunner(serviceConfigPath,serviceUrl,serviceClass);
        }
        return specificationRunner;
    }

    /**
     * For any given service type
     * @param serviceConfigPath
     * @param serviceUrl
     * @param serviceClass
     * @param serviceType
     */
    public static SpecificationRunner getInstance(String serviceConfigPath, String serviceUrl, Class<?> serviceClass, Service serviceType){
        if(specificationRunner == null){
            specificationRunner = new SpecificationRunner(serviceConfigPath,serviceUrl,serviceClass,serviceType);
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
    public List<Observation> run(String specFilePath)  {
        TestSpecification testSpecification = null;
        try {
            testSpecification = objectMapper.readValue(new File(specFilePath).getAbsoluteFile(), TestSpecification.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to map the given test specification "+e);
        }
        return httpTestOrchestrator.run(testSpecification);
    }


    /**
     * runs a test in a light weight fashion. Only clean up operation will be called and shutdown needs to be called by the test writer.
     *
     * @param specFilePath
     * @return
     */
    public List<Observation> runLite(String specFilePath){
        TestSpecification testSpecification = null;
        try {
            testSpecification = objectMapper.readValue(new File(specFilePath).getAbsoluteFile(), TestSpecification.class);
        } catch (Exception e) {
            throw new RuntimeException("Unable to map the given test specification "+e);
        }
        return runLite(testSpecification);
    }

    public List<Observation> runLite(TestSpecification testSpecification){
        return httpTestOrchestrator.runLite(testSpecification);
    }

    /**
     * shuts down all the dependencies that were initialized until now: This should be called in the end the of tests run => end of test suite run
     */
    public void shutDown() {
        try {
            CompositeDependencyRegistry.getInstance().shutDown();
        } catch (Exception e) {
            System.out.println("Error in shutting down all dependencies : You may face problems in next run");
        }
    }

}
