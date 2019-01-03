package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.DropwizardServiceStarter;
import com.flipkart.component.testing.SUT;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.servers.DependencyRegistry;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Runs a single test specification : Need to extend for multiple
 */
public class SpecificationRunner {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private SUT sut;


    public SpecificationRunner(String serviceConfigPath, String serviceUrl, Class<?> serviceClass) {
        this.sut = new DropwizardServiceStarter(serviceConfigPath, serviceUrl, serviceClass);
    }

    public SpecificationRunner(SUT sut) {
        this.sut = sut;
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
        return new HttpTestOrchestrator(sut).run(testSpecification);
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
            TestSpecification testSpecification = objectMapper.readValue(new File(filePath), TestSpecification.class);
            testSpecifications.add(testSpecification);
        }
        return new HttpTestOrchestrator(sut).chain(testSpecifications);
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
            TestSpecification testSpecification = objectMapper.readValue(new File(filePath), TestSpecification.class);
            testSpecifications.add(testSpecification);
        }
        return new HttpTestOrchestrator(sut).runMultiple(testSpecifications);
    }


    /**
     * runs a test in a light weight fashion. Only clean up operation will be called and shutdown needs to be called by the test writer.
     *
     * @param specFilePath
     * @return
     */
    public List<Observation> runLite(String specFilePath) throws Exception {
        TestSpecification testSpecification = objectMapper.readValue(new File(specFilePath), TestSpecification.class);
        return new HttpTestOrchestrator(sut).runLite(testSpecification, sut);
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

}
