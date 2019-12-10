package com.flipkart.component.testing;

import org.apache.poi.ss.formula.functions.T;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A fake spout in order to induce events to the topology. Preferred for
 * 1) Functional testing of the topology.
 * 2) Faster to write tests in case where spawing local spouts are difficult
 */
public class FakeSpout extends BaseRichSpout {
    /**
     * batch size which tuples should be emitted
     */
    private final int batchSize;

    /**
     * Each Tuple Entry will be emitted in each call to nextTuple of Spout.
     * Ideally a list size of 2 doesn't should be sufficient enough to test multiple emits by spout
     */
    private List<T> tupleEntries = new ArrayList();

    /**
     * FieldName with which the output fields will be declared
     */
    private final String fieldName;

    /**
     * tuple Index to track what tuples need to be emitted
     */
    private int tupleIndex = 0;

    private SpoutOutputCollector collector;

    public FakeSpout(String fieldName, int batchSize) {
        this.fieldName = fieldName;
        this.batchSize = batchSize;
    }

    public void addTuple(T value) {
        this.tupleEntries.add(value);
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.collector = spoutOutputCollector;
    }

    /**
     * emits all registered tuples
     */
    @Override
    public void nextTuple() {
        if (!this.isDone()) {
            if (this.isBatchEmit()) {
                List<T> tuples = this.prepareBatch();
                this.collector.emit(new Values(new Object[]{tuples}), UUID.randomUUID());
            } else {
                this.collector.emit(new Values(new Object[]{this.tupleEntries.get(this.tupleIndex)}), UUID.randomUUID());
            }

        }
    }

    private List<T> prepareBatch() {
        List<T> batch = new ArrayList();

        int index;
        for(index = this.tupleIndex; index < this.tupleEntries.size() && batch.size() < this.batchSize; ++index) {
            batch.add(this.tupleEntries.get(index));
        }

        this.tupleIndex = index;
        return batch;
    }

    /**
     * @return : returns true if all the tuples are acknowledged
     */
    private boolean isDone() {
        return this.tupleIndex >= this.tupleEntries.size();
    }

    private boolean isBatchEmit() {
        return this.batchSize > 0;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(new String[]{this.fieldName}));
    }

    /**
     * @return : returns the total number of tuples registered with this spout
     */
    public int tuplesRegistered() {
        return this.isBatchEmit() ? this.tupleEntries.size() / this.batchSize : this.tupleEntries.size();
    }



}
