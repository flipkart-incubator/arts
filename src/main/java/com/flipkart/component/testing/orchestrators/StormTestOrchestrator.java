package com.flipkart.component.testing.orchestrators;

import com.flipkart.component.testing.model.TestSpecification;
import com.flipkart.component.testing.servers.DependencyInitializer;
import com.flipkart.component.testing.model.Observation;
import com.flipkart.component.testing.storm.functional.DelegateSpout;
import com.flipkart.component.testing.storm.functional.TestableTopology;
import org.apache.storm.Config;
import org.apache.storm.topology.base.BaseRichSpout;

import java.util.List;
import java.util.Map;

/**
 * An orchestrator for Storm Topology
 */
public class StormTestOrchestrator extends BaseTestOrchestrator {

    private TestableTopology testableTopology;
    private StormLocalCluster stormLocalCluster;
    private DelegateSpout delegateSpout;

    public StormTestOrchestrator(TestableTopology testableTopology) {
        this.testableTopology = testableTopology;
        this.testableTopology.prepareTopology();
    }

    /**
     * modify the spouts and bolts as per configuration
     */
    @SuppressWarnings("unchecked")
    public List<Observation> execute(TestSpecification testSpecification, int tuplesToBeEmitted) throws Exception {

        // spawn the services required for testSpecification
        DependencyInitializer.getInstance(testSpecification).initialize();

        //load the indirect inputs
        testDataLoader.load(testSpecification.getIndirectInputs());

        //wrap the spouts to track the emitted tuples
        Map<String, BaseRichSpout> spouts = testableTopology.getSpouts();
        spouts.keySet().forEach(spoutId -> spouts.put(spoutId, wrapSpout(spouts, spoutId, tuplesToBeEmitted)));

        //start the storm cluster
        this.stormLocalCluster = new StormLocalCluster(testableTopology);
        this.stormLocalCluster.start();

        //wait for completion
        this.waitForCompletion();

        //collect the observations
        return this.observationCollector.actualObservations(testSpecification.getObservations());

    }


    public void cleanUp() {
        this.stormLocalCluster.tearDown();
    }

    /**
     * wrapping spout is needed to track the number of tuples to be emitted and acts as a trigger on when the topology execution is complete
     * @param spouts
     * @param spoutId
     * @param tuplesToBeEmitted
     * @return
     */
    private BaseRichSpout wrapSpout(Map<String, BaseRichSpout> spouts, String spoutId, int tuplesToBeEmitted) {
        BaseRichSpout spout = spouts.get(spoutId);
        this.delegateSpout = new DelegateSpout(spout, tuplesToBeEmitted);
        return this.delegateSpout;
    }


    /**
     * tracks the tuples to emit and waits for 1000 seconds before giving up
     * @throws InterruptedException
     */
    private void waitForCompletion() throws InterruptedException {
        int totalTimeWaited = 0;
        while (!this.delegateSpout.isDone() && totalTimeWaited < 100000) {
            System.out.println("waiting for completion : total Time Waited in seconds" + totalTimeWaited / 1000);
            Thread.sleep(1000);
            totalTimeWaited += 1000;
        }

        if (totalTimeWaited > 100000) {
            System.out.println("giving up: waited for" + totalTimeWaited / 1000);
        }
    }

}
