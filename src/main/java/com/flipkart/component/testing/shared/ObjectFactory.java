package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.internal.Constants;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;


//TODO:PAVAN see if all the operations can be moved out similar to Hbase and ES rather than exposing channel and connection for mysql and rmq
public class ObjectFactory {

    public static MysqlConnection getMysqlConnection(MysqlConnectionConfig mysqlConnectionConfig) {

        if (mysqlConnectionConfig.getConnectionType() == MysqlConnectionType.IN_MEMORY) {
            return new InMemoryConnection(mysqlConnectionConfig);
        } else if (mysqlConnectionConfig.getConnectionType() == MysqlConnectionType.LOCALHOST) {
            return new LocalhostConnection(mysqlConnectionConfig);
        }

        throw new IllegalStateException("Connection not registered for" + mysqlConnectionConfig.getConnectionType());
    }

    public static Channel getRMQChannel() throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Address[] addresses = new Address[]{new Address("localhost", Constants.RMQ_SERVER_PORT)};
        com.rabbitmq.client.Connection connection = factory.newConnection(addresses);
        return connection.createChannel();
    }

    public static HBaseAdminOperations getHBaseOperations(HbaseTestConfig hbaseTestConfig) {
        if (hbaseTestConfig.getConnectionType() == ConnectionType.IN_MEMORY) {
//            return LocalHBaseServerOperations.getInstance();
            throw new IllegalStateException("not yet implemented");
        }

        return new RemoteHBaseOperations();
    }

    public static RedisOperations getRedisOperations(RedisTestConfig redisTestConfig) {
        if (redisTestConfig.isSentinel()) {
            return SentinelBasedOperations.getFromPool(redisTestConfig);
        } else {
            return SingleHostBasedOperations.getInstance();
        }
    }

    public static ElasticSearchOperations getESOperations(ElasticSearchTestConfig elasticSearchTestConfig) {
        if (elasticSearchTestConfig.getConnectionType() == ConnectionType.IN_MEMORY) {
            return EmbeddedESOperations.getInstance(elasticSearchTestConfig);
        } else {
            return RemoteElasticSearchOperations.getInstance(elasticSearchTestConfig);
        }
    }

    public static HazelcastInstance getHazelcastInstance(HazelcastTestConfig hazelcastTestConfig){
        HazelcastInstance hazelcastInstance;
        if (hazelcastTestConfig.isServerMode()) {
            // if used in client server mode, we need to create new instance
            hazelcastInstance = new HazelcastInstanceInitializer(hazelcastTestConfig).getHazelcastInstance();
        } else {
            // or else will use the hazelcast instance initialized by client in its application code
            if(hazelcastTestConfig.isEmbeddedMode())
                hazelcastInstance = Hazelcast.getAllHazelcastInstances().iterator().next();
            else
                throw new IllegalArgumentException("Hazelcast initialized for incorrect mode !!");
        }
        return hazelcastInstance;
    }

    public static MockServerOperations getMockServerOperations(){
        return MockServerOperationsImpl.getInstance();
    }
}

