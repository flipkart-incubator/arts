package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.*;

import java.io.IOException;

/**
 * Holds the responsibility of collecting Observations: Generally after the test case execution from multiple Data Stores
 */
class ObservationCollectorImpl implements ObservationCollector {

    /**
     * extracts the actual Observation using attributes of expected observation
     *
     * @param expectedObservation
     * @return
     */
    @Override
    public Observation actualObservations(Observation expectedObservation) throws IOException {
        if (expectedObservation.isKafka()) {
            return new KafkaLocalConsumer().actualObservations((KafkaObservation) expectedObservation);
        } else if (expectedObservation.isRMQ()) {
            return new RMQLocalConsumer().actualObservations((RMQObservation) expectedObservation);
        } else if (expectedObservation.isMysql()) {
            return new MysqlConsumer().actualObservations((MysqlObservation) expectedObservation);
        } else if(expectedObservation.isHBase()){
            return new HBaseLocalConsumer().actualObservations((HBaseObservation) expectedObservation);
        } else if(expectedObservation.isHttp()){
            return new HttpResponseConsumer().actualObservations((HttpObservation) expectedObservation);
        } else if(expectedObservation.isRedis()) {
            return new RedisLocalConsumer().actualObservations((RedisObservation) expectedObservation);
        }else if(expectedObservation.isElasticSearch()){
            return new ElasticSearchLocalConsumer().actualObservations((ElasticSearchObservation) expectedObservation);
        }
        throw new IllegalStateException("Consumer is not defined for observation" + expectedObservation);
    }
}
