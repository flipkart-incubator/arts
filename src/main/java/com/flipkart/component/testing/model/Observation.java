package com.flipkart.component.testing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
        @JsonSubTypes.Type(value = ElasticSearchObservation.class, name = "elasticSearchObservation")
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


}
