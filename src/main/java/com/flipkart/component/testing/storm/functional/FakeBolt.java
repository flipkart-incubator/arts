package com.flipkart.component.testing.storm.functional;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A fake bolt that captures all the entries received by it.
 * Preferred to use this as a sink bolt in topology in cases
 * where original bolts are difficult to spawn
 */
public class FakeBolt extends BaseRichBolt {

    private OutputCollector collector;

    /**
     * the observations that are collected to verify
     */
    public static List<Tuple> observations = new CopyOnWriteArrayList<Tuple>();

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        this.observations.add(tuple);
        collector.ack(tuple);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
