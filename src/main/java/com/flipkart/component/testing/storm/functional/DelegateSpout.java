package com.flipkart.component.testing.storm.functional;

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

    private static Map<String, Integer> tupleTrackerMap = new HashMap<>();
    private final BaseRichSpout originalSpout;
    private final String trackerId;


    public DelegateSpout(BaseRichSpout spout, int tuples, String trackerId) {
        this.originalSpout = spout;
        this.trackerId = trackerId;
        if(tupleTrackerMap.containsKey(trackerId)){
            throw new IllegalArgumentException("trackerId already exists" + trackerId);
        }

        tupleTrackerMap.put(trackerId, tuples);

    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.originalSpout.open(map, topologyContext, spoutOutputCollector);
    }

    @Override
    public void nextTuple() {
        if (!isDone()) {
            this.originalSpout.nextTuple();
            relax(500);
        }else{
            relax(1000);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        this.originalSpout.declareOutputFields(outputFieldsDeclarer);
    }

    private void relax(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ack(Object msgId) {
        this.originalSpout.ack(msgId);
        int tuplesToBeEmitted = tupleTrackerMap.get(this.trackerId);
        tuplesToBeEmitted--;
        tupleTrackerMap.put(trackerId, tuplesToBeEmitted);
    }

    @Override
    public void activate(){
        this.originalSpout.activate();
    }

    @Override
    public void deactivate(){
        this.originalSpout.deactivate();
    }

    public boolean isDone(){
        return tupleTrackerMap.get(trackerId) <= 0;
    }

    @Override
    public void fail(Object msgId) {
        this.originalSpout.fail(msgId);
    }
}
