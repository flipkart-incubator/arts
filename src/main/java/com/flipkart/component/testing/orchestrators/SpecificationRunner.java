package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.DropwizardServiceStarter;
import com.flipkart.component.testing.SUT;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Runs a single test specification : Need to extend for multiple
 */
public class SpecificationRunner {
    private List<String> specFilePaths;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final SUT sut;

    /**
     * Specification Runner for Dropwizard service.
     *
     * @param specFilePath
     * @param serviceConfigPath
     */
    public SpecificationRunner(String specFilePath, String serviceConfigPath, String serviceUrl, Class<?> serviceClass) {
        this.specFilePaths = new ArrayList<>();
        this.specFilePaths.add(specFilePath);
        this.sut = new DropwizardServiceStarter(serviceConfigPath, serviceUrl, serviceClass);
    }

    public SpecificationRunner(String specFilePath, SUT sut) {
        this.specFilePaths = new ArrayList<>();
        this.specFilePaths.add(specFilePath);
        this.sut = sut;
    }

    public SpecificationRunner(List<String> specFilePaths, String serviceConfigPath, String serviceUrl, Class<?> serviceClass) {
        this.specFilePaths = specFilePaths;
        this.sut = new DropwizardServiceStarter(serviceConfigPath, serviceUrl, serviceClass);
    }

    public List<Observation> run() throws Exception {
        TestSpecification testSpecification = objectMapper.readValue(new File(specFilePaths.get(0)), TestSpecification.class);
        return new HttpTestOrchestrator().run(testSpecification, sut);
    }

    public Map<TestSpecification, List<Observation>> chain() throws Exception {
        List<TestSpecification> testSpecifications = new ArrayList<>();
        for (String filePath : specFilePaths) {
            TestSpecification testSpecification = objectMapper.readValue(new File(filePath), TestSpecification.class);
            testSpecifications.add(testSpecification);
        }
        return new HttpTestOrchestrator().chain(testSpecifications, sut);
    }

}
