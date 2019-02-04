package com.flipkart.component.testing.extractors;

import com.flipkart.component.testing.model.*;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchObservation;
import com.flipkart.component.testing.model.hbase.HBaseObservation;
import com.flipkart.component.testing.model.http.HttpIndirectObservation;
import com.flipkart.component.testing.model.http.HttpObservation;
import com.flipkart.component.testing.model.kafka.KafkaObservation;
import com.flipkart.component.testing.model.mysql.MysqlObservation;
import com.flipkart.component.testing.model.redis.RedisObservation;
import com.flipkart.component.testing.model.rmq.RMQObservation;
import com.flipkart.component.testing.model.zookeeper.ZookeeperObservation;

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
        }else if(expectedObservation.isAerospike()){
            return new AerospikeLocalConsumer().actualObservations((AerospikeObservation) expectedObservation);
        }else if(expectedObservation.isHttpIndirect()){
            return new HttpInteractionConsumer().actualObservations((HttpIndirectObservation) expectedObservation);
        }else if(expectedObservation.isZk()){
            return new ZookeeperConsumer().actualObservations((ZookeeperObservation) expectedObservation);
        }
        throw new IllegalStateException("Consumer is not defined for observation" + expectedObservation);
    }
}
