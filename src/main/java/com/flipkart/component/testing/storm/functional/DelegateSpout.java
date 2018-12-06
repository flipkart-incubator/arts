package com.flipkart.component.testing.storm.functional;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;

import java.util.Map;

/**
 * A spout which composes the original spout and delegates the operations
 * This spout tracks the tuples to be emitted so that it will be easy for the
 * test writer to wait for the completion of emitted tuples and then move on to the verifying the observations
 */
public class DelegateSpout extends BaseRichSpout {

    private final BaseRichSpout originalSpout;
    private static int tuplesToBeEmitted;


    public DelegateSpout(BaseRichSpout spout, int tuples) {
        this.originalSpout = spout;
        tuplesToBeEmitted = tuples;
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
        tuplesToBeEmitted--;
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
        return tuplesToBeEmitted == 0;
    }

    @Override
    public void fail(Object msgId) {
        this.originalSpout.fail(msgId);
    }
}
