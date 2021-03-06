package com.flipkart.component.testing.orchestrators;

import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.storm.functional.DelegateSpout;
import com.flipkart.component.testing.storm.functional.TestableTopology;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * An orchestrator for Storm Topology
 */
public class StormTestOrchestrator extends BaseTestOrchestrator {

    private TestableTopology testableTopology;
    private StormLocalCluster stormLocalCluster;
    private List<DelegateSpout> delegateSpouts = new ArrayList<>();


    public StormTestOrchestrator(TestableTopology testableTopology) {
        this.testableTopology = testableTopology;
        this.testableTopology.prepareTopology();
    }

    /**
     * executes a storm test as per specification
     * @param testSpecification
     * @param tuplesToBeEmitted : Number of tuples that are expected to be emitted
     * @return
     * @throws Exception
     */
    public List<Observation> execute(TestSpecification testSpecification, int tuplesToBeEmitted) throws Exception {
        try {
            return this.executeInner(testSpecification,tuplesToBeEmitted);
        } finally {
            this.shutDown();
        }
    }

    private void shutDown() {
        this.dependencyRegistry.shutDown();
    }

    /**
     * executes a storm test as per specification
     * @param testSpecification
     * @param tuplesToBeEmitted : Number of tuples that are expected to be emitted
     * @return
     * @throws Exception
     */
    public List<Observation> executeLite(TestSpecification testSpecification, int tuplesToBeEmitted) throws Exception {
        try {
            return this.executeInner(testSpecification,tuplesToBeEmitted);
        } finally {
//            if(stormLocalCluster != null){
////                stormLocalCluster.tearDown();
//            }
            dependencyRegistry.clean();
        }
    }



    @SuppressWarnings("unchecked")
    private List<Observation> executeInner(TestSpecification testSpecification, int tuplesToBeEmitted) throws Exception {
        // spawn the services required for testSpecification
        dependencyRegistry.registerAndInitialize(testSpecification);

        //load the indirect inputs
        testDataLoader.load(testSpecification.getIndirectInputs());

        //wrap the spouts to track the emitted tuples
        Map<String, BaseRichSpout> spouts = testableTopology.getSpouts();
        spouts.keySet().forEach(spoutId -> spouts.put(spoutId, wrapSpout(spouts, spoutId, tuplesToBeEmitted)));

        //start the storm cluster
        this.stormLocalCluster = new StormLocalCluster(testableTopology);
        this.stormLocalCluster.start();

        //wait for completion
        this.waitForCompletion(testSpecification.getTtlInMs());

        //collect the observations
        return this.observationCollector.actualObservations(testSpecification.getObservations());
    }


    /**
     * wrapping spout is needed to track the number of tuples to be emitted and acts as a trigger on when the topology execution is complete
     *
     * @param spouts
     * @param spoutId
     * @param tuplesToBeEmitted
     * @return
     */
    private BaseRichSpout wrapSpout(Map<String, BaseRichSpout> spouts, String spoutId, int tuplesToBeEmitted) {
        BaseRichSpout spout = spouts.get(spoutId);
        DelegateSpout delegateSpout = new DelegateSpout(spout, tuplesToBeEmitted, testableTopology.getTopologyName() + '_' + UUID.randomUUID().toString());
        this.delegateSpouts.add(delegateSpout);
        return delegateSpout;
    }


    /**
     * tracks the tuples to emit and waits for 1000 seconds before giving up
     *
     * @throws InterruptedException
     */
    private void waitForCompletion(int ttl) throws Exception {
        int totalTimeWaited = 0;
        while (!isCompleted() && totalTimeWaited < ttl) {
            System.out.println("waiting for completion : total Time Waited in seconds" + totalTimeWaited / 1000);
            Thread.sleep(1000);
            totalTimeWaited += 1000;
        }

        if (totalTimeWaited > ttl) {
            System.out.println("giving up: waited for" + totalTimeWaited / 1000);
            throw new Exception("ttl "+ ttl/1000 + "s expired");
        }
    }


    /**
     * returns true if the topology has completed execution.
     * @return
     */
    private boolean isCompleted(){
        //ideally should be allMatch: but it is not straightforward for now
        return this.delegateSpouts.stream().anyMatch(DelegateSpout::isDone);
    }

}
