package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.DropwizardServiceStarter;
import com.flipkart.component.testing.SUT;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.servers.DependencyRegistry;
import com.flipkart.component.testing.storm.functional.TestableTopology;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.flipkart.component.testing.shared.ObjectFactory.OBJECT_MAPPER;

/**
 * Runs a single test specification : Need to extend for multiple
 */
public class SpecificationRunner {
    private final HttpTestOrchestrator httpTestOrchestrator;


    public SpecificationRunner(String serviceConfigPath, String serviceUrl, Class<?> serviceClass) {
        SUT sut = new DropwizardServiceStarter(serviceConfigPath, serviceUrl, serviceClass);
        httpTestOrchestrator = new HttpTestOrchestrator(sut, new HttpTestRunner());
    }

    public SpecificationRunner(SUT sut, HttpTestRunner httpTestRunner) {
        httpTestOrchestrator = new HttpTestOrchestrator(sut, httpTestRunner);
    }

    /**
     * runs a single test
     *
     * @param specFilePath
     * @return
     * @throws Exception
     */
    public List<Observation> run(String specFilePath) throws Exception {
        TestSpecification testSpecification = OBJECT_MAPPER.readValue(new File(specFilePath), TestSpecification.class);
        return httpTestOrchestrator.run(testSpecification);
    }

    /**
     * runs a single test by chaining them : after run of each test there won't be any clean up and shutdown of dependencies
     *
     * @param specFilePaths
     * @return
     * @throws Exception
     */
    public Map<TestSpecification, List<Observation>> chain(List<String> specFilePaths) throws Exception {
        List<TestSpecification> testSpecifications = new ArrayList<>();
        for (String filePath : specFilePaths) {
            TestSpecification testSpecification = OBJECT_MAPPER.readValue(new File(filePath), TestSpecification.class);
            testSpecifications.add(testSpecification);
        }
        return httpTestOrchestrator.chain(testSpecifications);
    }

    /**
     * runs multiple tests : after each spec run there will be a clean up and in the end there will be a shutdown
     *
     * @param specFilePaths
     * @return
     * @throws Exception
     */
    public LinkedHashMap<TestSpecification, List<Observation>> runMultipleTests(List<String> specFilePaths) throws Exception {
        List<TestSpecification> testSpecifications = new ArrayList<>();
        for (String filePath : specFilePaths) {
            TestSpecification testSpecification = OBJECT_MAPPER.readValue(new File(filePath), TestSpecification.class);
            testSpecifications.add(testSpecification);
        }
        return httpTestOrchestrator.runMultiple(testSpecifications);
    }


    /**
     * runs a test in a light weight fashion. Only clean up operation will be called and shutdown needs to be called by the test writer.
     *
     * @param specFilePath
     * @return
     */
    public List<Observation> runLite(String specFilePath) throws Exception {
        TestSpecification testSpecification = OBJECT_MAPPER.readValue(new File(specFilePath), TestSpecification.class);
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
            DependencyRegistry.INSTANCE.shutDown();
        } catch (Exception e) {
            System.out.println("Error in shutting down all dependencies : You may face problems in next run");
        }
    }


    /**
     * runLite for a storm topology
     * @param testableTopology
     * @param specFile
     * @param tuplesToBeEmitted
     */
    public List<Observation> runLite(TestableTopology testableTopology, String specFile, int tuplesToBeEmitted) throws Exception {
        TestSpecification testSpecification = OBJECT_MAPPER.readValue(new File(specFile), TestSpecification.class);
        List<Observation> observations = new StormTestOrchestrator(testableTopology).executeLite(testSpecification, tuplesToBeEmitted);
        return observations;
    }

}
