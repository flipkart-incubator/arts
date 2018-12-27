package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.servers.DependencyInitializer;
import com.flipkart.component.testing.internal.HttpTestRunner;
import com.flipkart.component.testing.SUT;
import com.flipkart.component.testing.model.http.HttpDirectInput;
import com.flipkart.component.testing.model.Observation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Single entry point for the test writer to orchestrate the test set up
 * and retrieving the observations for Api based test cases.
 */
public class HttpTestOrchestrator extends BaseTestOrchestrator {

    private final HttpTestRunner testRunner;
    private ObjectMapper objectMapper = new ObjectMapper();

    public HttpTestOrchestrator() {
        testRunner = new HttpTestRunner();
    }

    //For Test Purpose
    HttpTestOrchestrator(HttpTestRunner httpTestRunner) {
        this.testRunner = httpTestRunner;
    }

    /**
     * Replays the testDataFile to the url specified
     *
     * @param testDataFile
     * @throws IOException
     */
    public void replay(File testDataFile, SUT sut) throws Exception {
        RecordLoader recordLoader = new RecordLoader(testDataFile);
        while (recordLoader.hasNext()) {
            TestSpecification testSpecification = recordLoader.next();
            run(testSpecification, sut);
        }
    }

    /**
     * run the test for the SUT
     *
     * @param testSpecification
     * @param sut
     */
    @SuppressWarnings("unchecked")
    public List<Observation> run(TestSpecification testSpecification, SUT sut) throws Exception {

        //spawn the services required for the test
        DependencyInitializer dependencyInitializer = DependencyInitializer.getInstance(testSpecification);
        try {
            dependencyInitializer.initialize();

            //start the system under test
            sut.start();

            //load the data into the dependencies
            this.testDataLoader.load(testSpecification.getIndirectInputs());

            //http test runner => making a http request
            testRunner.run((HttpDirectInput) testSpecification.getDirectInput(), sut.getUrl());

            List<Observation> list = this.observationCollector.actualObservations(testSpecification.getObservations());

            return list;
        } finally {
            try {
               dependencyInitializer.shutDown();
            } catch (Exception e) {
                System.out.println("Error in shutting down all dependencies : You may face problems in next run");
            }
        }
    }


    @SuppressWarnings("unchecked")
    public Map<TestSpecification, List<Observation>> chain(Iterable<TestSpecification> testSpecifications, SUT sut) throws Exception {
        DependencyInitializer dependencyInitializer = DependencyInitializer.getInstance(testSpecifications);
        try {
            dependencyInitializer.initialize();
            //start the system under test
            sut.start();

            Map<TestSpecification, List<Observation>> map = new HashMap<>();

            for (TestSpecification testSpecification : testSpecifications) {
                this.testDataLoader.load(testSpecification.getIndirectInputs());

                //http test runner => making a http request
                testRunner.run((HttpDirectInput) testSpecification.getDirectInput(), sut.getUrl());

                List<Observation> list = this.observationCollector.actualObservations(testSpecification.getObservations());

                map.put(testSpecification, list);
            }
            return map;
        } finally {
            try {
                dependencyInitializer.shutDown();
            } catch (Exception e) {
                System.out.println("Error in shutting down all dependencies : You may face problems in next run");
            }
        }
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
