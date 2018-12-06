package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.internal.Constants;
import com.flipkart.component.testing.model.ConnectionType;
import com.flipkart.component.testing.model.mysql.MysqlConnectionType;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

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
            return new SingleHostBasedOperations();
        }
    }

    public static ElasticSearchOperations getESOperations(ElasticSearchTestConfig elasticSearchTestConfig) {
        if (elasticSearchTestConfig.getConnectionType() == ConnectionType.IN_MEMORY) {
            return EmbeddedESOperations.getInstance(elasticSearchTestConfig);
        } else {
            return RemoteElasticSearchOperations.getInstance(elasticSearchTestConfig);
        }
    }

    public static MockServerOperations getMockServerOperations(){
        return MockServerOperationsImpl.getInstance();
    }
}
