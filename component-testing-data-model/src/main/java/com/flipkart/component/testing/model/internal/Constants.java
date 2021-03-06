package com.flipkart.component.testing.model.internal;

/**
 * Constants file for the test orchestration
 */
public class Constants {

    //TODO:PAVAN remove this class
    public static final int RMQ_SERVER_PORT = 5672;
    public static final int KAFKA_BROKER_PORT = 9092;
    public static final int ZOOKEEPER_PORT = 2181;
    public static final int ZKR_TICK_TIME = 2000;
    public static final int ZKR_CONNECTIONS = 5;
    public static final int MAX_MESSAGES_TO_FETCH_FROM_KAFKA = 10000;
    public static final String BROKER_HOST = "localhost:" + KAFKA_BROKER_PORT;
    public static final int HTTP_MOCK_SERVER_PORT = 7777;
    public static final int REDIS_SERVER_PORT = 6379;
    public static final String ES_CLUSTER_PORT = "9210";
    public static final String ES_CLUSTER_VERSION = "1.5.2";
    public static final String ES_CLIENT_HOST = "127.0.0.1";
    public static final int ES_CLIENT_PORT =9300;
    public static final String ES_CLUSTER_NAME = "elasticsearch";
    public static final String SOLR_SERVER_PORT = "8983";
    public static final String HZ_INSTANCE_NAME = "regression";
    public static final String HZ_MAPS_DS = "maps";
    public static final String HZ_LOGGING_PROPERTY = "hazelcast.logging.type";
    public static final String HZ_IMAP_CLASS = "com.hazelcast.map.impl.proxy.MapProxyImpl";


}
