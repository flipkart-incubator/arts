package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flipkart.component.testing.model.aerospike.AerospikeObservation;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchObservation;
import com.flipkart.component.testing.model.hazelcast.HazelcastObservation;
import com.flipkart.component.testing.model.hbase.HBaseObservation;
import com.flipkart.component.testing.model.http.HttpIndirectObservation;
import com.flipkart.component.testing.model.http.HttpObservation;
import com.flipkart.component.testing.model.kafka.KafkaObservation;
import com.flipkart.component.testing.model.mysql.MysqlObservation;
import com.flipkart.component.testing.model.redis.RedisObservation;
import com.flipkart.component.testing.model.rmq.RMQObservation;
import com.flipkart.component.testing.model.zookeeper.ZookeeperObservation;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "name")
@JsonSubTypes({
        @JsonSubTypes.Type(value = KafkaObservation.class, name = "kafkaObservation"),
        @JsonSubTypes.Type(value = RMQObservation.class, name = "rmqObservation"),
        @JsonSubTypes.Type(value = HttpObservation.class, name = "httpObservation"),
        @JsonSubTypes.Type(value = MysqlObservation.class, name = "mysqlObservation"),
        @JsonSubTypes.Type(value = HBaseObservation.class, name = "hbaseObservation"),
        @JsonSubTypes.Type(value = RedisObservation.class, name = "redisObservation"),
        @JsonSubTypes.Type(value = ElasticSearchObservation.class, name = "elasticSearchObservation"),
        @JsonSubTypes.Type(value = AerospikeObservation.class, name = "AerospikeObservation"),
        @JsonSubTypes.Type(value = HttpIndirectObservation.class, name = "httpIndirectObservation"),
        @JsonSubTypes.Type(value = HazelcastObservation.class, name = "hazelcastObservation"),
        @JsonSubTypes.Type(value = HttpIndirectObservation.class, name = "zookeeperObservation")

})
public interface Observation {

    @JsonIgnore
    default boolean isRMQ() {
        return this instanceof RMQObservation;
    }

    @JsonIgnore
    default boolean isKafka() {
        return this instanceof KafkaObservation;
    }

    @JsonIgnore
    default boolean isMysql() {
        return this instanceof MysqlObservation;
    }

    @JsonIgnore
    default boolean isHBase() {
        return this instanceof HBaseObservation;
    }

    @JsonIgnore
    default boolean isHttp(){
        return this instanceof HttpObservation;
    }

    @JsonIgnore
    default boolean isRedis() {
        return this instanceof RedisObservation;
    }

    @JsonIgnore
    default boolean isElasticSearch(){
        return this instanceof ElasticSearchObservation;
    }

    @JsonIgnore
    default boolean isAerospike(){
        return this instanceof AerospikeObservation;
    }

    @JsonIgnore
    default boolean isHttpIndirect() {return this instanceof HttpIndirectObservation;}

    @JsonIgnore
    default boolean isHazelcast() {
        return this instanceof HazelcastObservation;
    }

    @JsonIgnore
    default boolean isZk() {
        return this instanceof ZookeeperObservation;
    }

}
