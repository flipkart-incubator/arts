package com.flipkart.component.testing.storm.functional;

import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;

import java.util.Map;

/**
 * Interface that defines the contract for a topology to be testable
 */
public interface TestableTopology {

    /**
     * @return return the final config that needs to be submitted to storm topology
     */
    Config getTopologyConf();

    /**
     * @return returns the topology Name
     */
    String getTopologyName();

    /**
     * prepares the topology: Build the map internally inside topology which are returned via getSpouts and getSinkBolts methods below
     */
    void prepareTopology();

    /**
     * @return build the DAG associated with the topology
     * the maps below getSpouts and getSinkBolts should be used to build topology
     */
    StormTopology buildTopology();

    /**
     * @return returns the source spouts associated with a topology
     */
    Map<String, BaseRichSpout> getSpouts();

    /**
     * @return returns the sink bolts associated with a topology
     */
    Map<String, BaseRichBolt> getSinkBolts();

}
