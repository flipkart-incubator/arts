package com.flipkart.component.testing;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;

import java.util.HashMap;
import java.util.Map;

/**
 * A spout which composes the original spout and delegates the operations
 * This spout tracks the tuples to be emitted so that it will be easy for the
 * test writer to wait for the completion of emitted tuples and then move on to the verifying the observations
 */
public class DelegateSpout extends BaseRichSpout {
    private static Map<String, Integer> tupleTrackerMap = new HashMap();
    private final BaseRichSpout originalSpout;
    private final String trackerId;

    public DelegateSpout(BaseRichSpout spout, int tuples, String trackerId) {
        this.originalSpout = spout;
        this.trackerId = trackerId;
        if (tupleTrackerMap.containsKey(trackerId)) {
            throw new IllegalArgumentException("trackerId already exists" + trackerId);
        } else {
            tupleTrackerMap.put(trackerId, tuples);
        }
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.originalSpout.open(map, topologyContext, spoutOutputCollector);
    }

    @Override
    public void nextTuple() {
        if (!this.isDone()) {
            this.originalSpout.nextTuple();
            this.relax(500);
        } else {
            this.relax(1000);
        }

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        this.originalSpout.declareOutputFields(outputFieldsDeclarer);
    }

    private void relax(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException var3) {
            var3.printStackTrace();
        }

    }

    @Override
    public void ack(Object msgId) {
        this.originalSpout.ack(msgId);
        int tuplesToBeEmitted = (Integer)tupleTrackerMap.get(this.trackerId);
        --tuplesToBeEmitted;
        tupleTrackerMap.put(this.trackerId, tuplesToBeEmitted);
    }

    @Override
    public void activate() {
        this.originalSpout.activate();
    }

    @Override
    public void deactivate() {
        this.originalSpout.deactivate();
    }

    public boolean isDone() {
        return tupleTrackerMap.get(this.trackerId) <= 0;
    }

    @Override
    public void fail(Object msgId) {
        this.originalSpout.fail(msgId);
    }
}
