package com.flipkart.component.testing.orchestrators;


import com.flipkart.component.testing.storm.functional.TestableTopology;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;

/**
 * A wrapper over localCluster storm topology:
 * 1) Collaborates with Testable topology for preparing the topology
 * 2) Submits the prepared topology to the local cluster
 * 3) Kills topology and shutdown cluster
 */
class StormLocalCluster {

    private LocalCluster localCluster;
    private final TestableTopology testableTopology;

    StormLocalCluster(TestableTopology testableTopology) {
        localCluster = new LocalCluster();
        this.testableTopology = testableTopology;
    }


    public void start() {
        StormTopology topologyToSubmit = this.testableTopology.buildTopology();
        localCluster.submitTopology(this.testableTopology.getTopologyName(), this.testableTopology.getTopologyConf(), topologyToSubmit);
    }

    public void tearDown() {
        try {
            if (localCluster != null) {
                localCluster.killTopology(testableTopology.getTopologyName());
                localCluster.shutdown();
                localCluster = null;
            }
        } catch (Exception ex) {
        }
    }


}
