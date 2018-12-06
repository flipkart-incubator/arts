package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.flipkart.component.testing.model.aerospike.AerospikeIndirectInput;
import com.flipkart.component.testing.model.elasticsearch.ElasticSearchIndirectInput;
import com.flipkart.component.testing.model.hbase.HBaseIndirectInput;
import com.flipkart.component.testing.model.http.HttpIndirectInput;
import com.flipkart.component.testing.model.kafka.KafkaIndirectInput;
import com.flipkart.component.testing.model.mysql.MysqlIndirectInput;
import com.flipkart.component.testing.model.redis.RedisIndirectInput;
import com.flipkart.component.testing.model.rmq.RMQIndirectInput;
import com.flipkart.component.testing.model.zookeeper.ZookeeperIndirectInput;

/**
 * Interface for Indirect Input
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "name")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HttpIndirectInput.class, name = "httpIndirectInput"),
        @JsonSubTypes.Type(value = KafkaIndirectInput.class, name = "kafkaIndirectInput"),
        @JsonSubTypes.Type(value = MysqlIndirectInput.class, name = "mysqlIndirectInput"),
        @JsonSubTypes.Type(value = RMQIndirectInput.class, name = "rmqIndirectInput"),
        @JsonSubTypes.Type(value = HBaseIndirectInput.class, name = "hbaseIndirectInput"),
        @JsonSubTypes.Type(value = RedisIndirectInput.class, name = "redisIndirectInput"),
        @JsonSubTypes.Type(value = ElasticSearchIndirectInput.class,name = "elasticSearchIndirectInput"),
        @JsonSubTypes.Type(value = ZookeeperIndirectInput.class, name = "zookeeperIndirectInput"),
        @JsonSubTypes.Type(value = AerospikeIndirectInput.class, name = "aerospikeIndirectInput"),

})
public interface IndirectInput {

    @JsonIgnore
    default boolean isHttpInput() {
        return this instanceof HttpIndirectInput;
    }

    @JsonIgnore
    default boolean isKafkaInput() {
        return this instanceof KafkaIndirectInput;
    }

    @JsonIgnore
    default boolean isMysqlInput() {
        return this instanceof MysqlIndirectInput;
    }

    @JsonIgnore
    default boolean isRMQInput() {
        return this instanceof RMQIndirectInput;
    }

    @JsonIgnore
    default boolean isHBaseInput() {
        return this instanceof HBaseIndirectInput;
    }

    @JsonIgnore
    default boolean isRedisInput() {
        return this instanceof RedisIndirectInput;
    }

    @JsonIgnore
    default boolean isElasticSearchInput() {
        return this instanceof ElasticSearchIndirectInput;
    }

    @JsonIgnore
    default boolean isZkInput() {return this instanceof ZookeeperIndirectInput;}

    @JsonIgnore
    default boolean isAerospikeInput() {return this instanceof AerospikeIndirectInput;}


}
