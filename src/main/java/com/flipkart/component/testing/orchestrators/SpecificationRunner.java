package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.DropwizardServiceStarter;
import com.flipkart.component.testing.SUT;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.orchestrators.HttpTestOrchestrator;

import java.io.File;
import java.util.List;

/**
 * Runs a single test specification : Need to extend for multiple
 */
public class SpecificationRunner {
    private final String specFilePath;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private final SUT sut;

    /**
     * Specification Runner for Dropwizard service.
     * @param specFilePath
     * @param serviceConfigPath
     */
    public SpecificationRunner(String specFilePath, String serviceConfigPath, String serviceUrl, Class<?> serviceClass) {
        this.specFilePath = specFilePath;
        this.sut = new DropwizardServiceStarter(serviceConfigPath, serviceUrl, serviceClass);
    }

    public SpecificationRunner(String specFilePath, SUT sut){
        this.specFilePath = specFilePath;
        this.sut = sut;
    }

    public List<Observation> run() throws Exception {
        TestSpecification testSpecification = objectMapper.readValue(new File(specFilePath), TestSpecification.class);
        return new HttpTestOrchestrator().run(testSpecification, sut);
    }

}
