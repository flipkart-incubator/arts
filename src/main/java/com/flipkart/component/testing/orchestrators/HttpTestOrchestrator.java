package com.flipkart.component.testing.orchestrators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.servers.DependencyInitializer;
import com.flipkart.component.testing.HttpTestRunner;
import com.flipkart.component.testing.SUT;
import com.flipkart.component.testing.model.HttpDirectInput;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
            TestData testData = recordLoader.next();
            run(testData, sut);
        }
    }

    /**
     * run the test for the SUT
     *
     * @param testData
     * @param sut
     */
    @SuppressWarnings("unchecked")
    public List<Observation> run(TestData testData, SUT sut) throws Exception {

        //spawn the services required for the test
        DependencyInitializer dependencyInitializer = DependencyInitializer.getInstance(testData);
        try {
            dependencyInitializer.initialize();

            //start the system under test
            sut.start();

            //load the data into the dependencies
            this.testDataLoader.load(testData.getIndirectInputs());

            //http test runner => making a http request
            testRunner.run((HttpDirectInput) testData.getDirectInput(), sut.getUrl());

            List<Observation> list = this.observationCollector.actualObservations(testData.getObservations());

            return list;
        } finally {
            try {
                dependencyInitializer.shutDown();
            } catch (Exception e) {
                System.out.println("Error in shutting down all dependencies : You may face problems in next run");
            }
        }
    }


    /**
     * Loads the records from a file line by line: Each line corresponds to schema as defined in TestData.java
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

        TestData next() throws IOException {
            TestData testData = objectMapper.readValue(line, TestData.class);
            line = bufferedReader.readLine();
            return testData;
        }
    }

}
