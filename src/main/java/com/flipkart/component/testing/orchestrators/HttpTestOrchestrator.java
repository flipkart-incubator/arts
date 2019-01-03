package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.SUT;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.model.http.HttpDirectInput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Single entry point for the test writer to orchestrate the test set up
 * and retrieving the observations for Api based test cases.
 */
public class HttpTestOrchestrator extends BaseTestOrchestrator {

    private final HttpTestRunner testRunner;
    private final SUT sut;

    private ObjectMapper objectMapper = new ObjectMapper();

    public HttpTestOrchestrator(SUT sut) {
        testRunner = new HttpTestRunner();
        this.sut = sut;
    }

    //For Test Purpose
    HttpTestOrchestrator(HttpTestRunner httpTestRunner) {
        this.testRunner = httpTestRunner;
        this.sut = () -> "";
    }

    /**
     * run the test for the SUT
     *
     * @param testSpecification
     */
    public List<Observation> run(TestSpecification testSpecification) throws Exception {

        //spawn the services required for the test

        try {
            dependencyRegistry.registerAndInitialize(testSpecification);
            sut.start();
            return loadRunCollect(testSpecification, sut);
        } finally {
            try {
                dependencyRegistry.shutDown();
            } catch (Exception e) {
                System.out.println("Error in shutting down all dependencies : You may face problems in next run");
            }
        }
    }


    /**
     * chain the list of specifications : initialize once: shutdown once: run multiple tests => chaining
     *
     * @param testSpecifications
     * @return
     * @throws Exception
     */
    public Map<TestSpecification, List<Observation>> chain(Iterable<TestSpecification> testSpecifications) throws Exception {
        Map<TestSpecification, List<Observation>> map = new HashMap<>();
        try {
            dependencyRegistry.registerAndInitialize(testSpecifications);
            sut.start();
            for (TestSpecification testSpecification : testSpecifications) {
                List<Observation> list = loadRunCollect(testSpecification, sut);
                map.put(testSpecification, list);
            }
            return map;
        } finally {
            try {
                dependencyRegistry.shutDown();
            } catch (Exception e) {
                System.out.println("Error in shutting down all dependencies : You may face problems in next run");
            }
        }
    }


    /**
     * run multiple tests: initialize & shutdown  dependencies for each test
     *
     * @param testSpecifications
     * @return
     * @throws Exception
     */
    public LinkedHashMap<TestSpecification, List<Observation>> runMultiple(List<TestSpecification> testSpecifications) {
        LinkedHashMap<TestSpecification, List<Observation>> observations = new LinkedHashMap<>();
        try {
            for (TestSpecification testSpecification : testSpecifications) {
                try {
                    observations.put(testSpecification, runLite(testSpecification, sut));
                } catch (Exception e) {
                    //not to block other tests
                    observations.put(testSpecification, null);
                } finally {
                    dependencyRegistry.clean();
                }
            }
        } finally {
            try {
                dependencyRegistry.shutDown();
            } catch (Exception e) {
                System.out.println("Error in shutting down all dependencies : You may face problems in next run");
            }
        }

        return observations;

    }


    /**
     * A lite weight run of a test case cleaning up the dependency after started.
     * @param testSpecification
     * @param sut
     * @return
     * @throws Exception
     */
    public List<Observation> runLite(TestSpecification testSpecification, SUT sut) throws Exception {
        try{
            dependencyRegistry.registerAndInitialize(testSpecification);
            sut.start();
            return loadRunCollect(testSpecification, sut);
        } finally {
            dependencyRegistry.clean();
        }
    }


    @SuppressWarnings("unchecked")
    private List<Observation> loadRunCollect(TestSpecification testSpecification, SUT sut) throws IOException {
        this.testDataLoader.load(testSpecification.getIndirectInputs());
        //http test runner => making a http request
        testRunner.run((HttpDirectInput) testSpecification.getDirectInput(), sut.getUrl());
        return this.observationCollector.actualObservations(testSpecification.getObservations());
    }


    /**
     * Loads the records from a file line by line: Each line corresponds to schema as defined in TestSpecification.java
     */
    class RecordLoader {

        private final BufferedReader bufferedReader;
        private String line;

        RecordLoader(File file) throws IOException {
            this.bufferedReader = new BufferedReader(new FileReader(file));
            this.line = bufferedReader.readLine();
        }


        boolean hasNext() {
            return line != null;
        }

        TestSpecification next() throws IOException {
            TestSpecification testSpecification = objectMapper.readValue(line, TestSpecification.class);
            line = bufferedReader.readLine();
            return testSpecification;
        }
    }

}
