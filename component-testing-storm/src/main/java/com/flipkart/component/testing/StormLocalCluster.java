package com.flipkart.component.testing;

import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;

/**
 * A wrapper over localCluster storm topology:
 * 1) Collaborates with Testable topology for preparing the topology
 * 2) Submits the prepared topology to the local cluster
 * 3) Kills topology and shutdown cluster
 */
public class StormLocalCluster {
    private LocalCluster localCluster = new LocalCluster();
    private final TestableTopology testableTopology;

    StormLocalCluster(TestableTopology testableTopology) {
        this.testableTopology = testableTopology;
    }

    public void start() {
        StormTopology topologyToSubmit = this.testableTopology.buildTopology();
        this.localCluster.submitTopology(this.testableTopology.getTopologyName(), this.testableTopology.getTopologyConf(), topologyToSubmit);
    }

    public void tearDown() {
        try {
            if (this.localCluster != null) {
                this.localCluster.killTopology(this.testableTopology.getTopologyName());
                this.localCluster.shutdown();
                this.localCluster = null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
