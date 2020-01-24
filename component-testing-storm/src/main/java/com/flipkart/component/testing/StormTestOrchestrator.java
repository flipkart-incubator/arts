package com.flipkart.component.testing;

import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.model.TestSpecification;
import org.apache.storm.topology.base.BaseRichSpout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
     *
     * @param testSpecification
     * @param tuplesToBeEmitted : Number of tuples that are expected to be emitted
     * @return
     * @throws Exception
     */
    public List<Observation> execute(TestSpecification testSpecification, int tuplesToBeEmitted) throws Exception {
        List<Observation> observations;
        try {
            observations = this.executeInner(testSpecification, tuplesToBeEmitted);
        } finally {
            this.shutDown();
        }
        return observations;
    }

    private List<Observation> executeInner(TestSpecification testSpecification, int tuplesToBeEmitted) throws Exception {
        try {
            this.dependencyRegistry.registerDependencies(testSpecification);
            this.testDataLoader.load(testSpecification.getIndirectInputs());
            Map<String, BaseRichSpout> spouts = this.testableTopology.getSpouts();
            spouts.keySet().forEach((spoutId) -> {
                BaseRichSpout baseRichSpout = spouts.put(spoutId, this.wrapSpout(spouts, spoutId, tuplesToBeEmitted));
            });
            this.stormLocalCluster = new StormLocalCluster(this.testableTopology);
            this.stormLocalCluster.start();
            this.waitForCompletion(testSpecification.getTtlInMs());
            return this.observationCollector.actualObservations(testSpecification.getObservations());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (testSpecification.getShouldClean()) {
                try {
                    dependencyRegistry.clean();
                } catch (Exception e) {
                    System.out.println("Exception while cleaning " + e);
                    e.printStackTrace();
                }
            }
        }
    }


    private void shutDown() {
        this.dependencyRegistry.shutDown();
    }


    /**
     * executes a storm test as per specification
     *
     * @param testSpecification
     * @param tuplesToBeEmitted : Number of tuples that are expected to be emitted
     * @return
     * @throws Exception
     */
    public List<Observation> executeLite(TestSpecification testSpecification, int tuplesToBeEmitted) throws Exception {
        List<Observation> observations;
        try {
            observations = this.executeInner(testSpecification, tuplesToBeEmitted);
        } finally {
            this.dependencyRegistry.shutDown();
        }
        return observations;
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
        BaseRichSpout spout = (BaseRichSpout) spouts.get(spoutId);
        DelegateSpout delegateSpout = new DelegateSpout(spout, tuplesToBeEmitted, this.testableTopology.getTopologyName() + '_' + UUID.randomUUID().toString());
        this.delegateSpouts.add(delegateSpout);
        return delegateSpout;
    }

    /*
     * tracks tuples to emit and waits for 1000 seconds before giving up
     * @throws InterruptedException
     */
    private void waitForCompletion(int ttl) throws Exception {
        int totalTimeWaited;
        for (totalTimeWaited = 0; !this.isCompleted() && totalTimeWaited < ttl; totalTimeWaited += 1000) {
            System.out.println("waiting for completion : total Time Waited in seconds" + totalTimeWaited / 1000);
            Thread.sleep(1000L);
        }

        if (totalTimeWaited > ttl) {
            System.out.println("giving up: waited for" + totalTimeWaited / 1000);
            throw new Exception("ttl " + ttl / 1000 + "s expired");
        }
    }

    private boolean isCompleted() {
        return this.delegateSpouts.stream().anyMatch(DelegateSpout::isDone);
    }
}