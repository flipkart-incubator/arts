package com.flipkart.component.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;

import java.util.List;

/**
 * Single entry point for the test writer to orchestrate the test set up
 * and retrieving the observations for Api based test cases.
 */
class HttpTestOrchestrator extends BaseTestOrchestrator {

    private final SUT sut;
    private ObjectMapper objectMapper = new ObjectMapper();

    HttpTestOrchestrator(SUT sut) {
        this.sut = sut;
    }

    /**
     * run the test for the SUT
     *
     * @param testSpecification
     */
    @SuppressWarnings("unchecked")
    public List<Observation> run(TestSpecification testSpecification){
        //spawn the services required for the test
        try {
            dependencyRegistry.registerDependencies(testSpecification);
            this.testDataLoader.load(testSpecification.getIndirectInputs());
            sut.start();
            return this.observationCollector.actualObservations(testSpecification.sanitizeObservations(sut.getUrl()));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                dependencyRegistry.shutDown();
            } catch (Exception e) {
                System.out.println("Error in shutting down all dependencies : You may face problems in next run");
            }
        }
    }

    /**
     * A lite weight run of a test case cleaning up the dependency after started.
     * @param testSpecification
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Observation> runLite(TestSpecification testSpecification) {
        try{
            dependencyRegistry.registerDependencies(testSpecification);
            this.testDataLoader.load(testSpecification.getIndirectInputsToBePreLoaded());
            sut.start();
            this.testDataLoader.load(testSpecification.getIndirectInputsToBePostLoaded());
            return this.observationCollector.actualObservations(testSpecification.sanitizeObservations(sut.getUrl()));
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(testSpecification.getShouldClean()){
                try{
                    dependencyRegistry.clean();
                }catch(Exception e){
                    System.out.println("Exception while cleaning " +  e);
                    e.printStackTrace();
                }

            }

        }
    }


}
