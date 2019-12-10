package com.flipkart.component.testing;

import org.apache.storm.Config;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.topology.base.BaseRichSpout;

import java.util.Map;

public interface TestableTopology {
    Config getTopologyConf();

    String getTopologyName();

    void prepareTopology();

    StormTopology buildTopology();

    Map<String, BaseRichSpout> getSpouts();

    Map<String, BaseRichBolt> getSinkBolts();
}
