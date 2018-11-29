package com.flipkart.component.testing.servers;


import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

import java.io.File;
import java.util.Properties;

import static com.flipkart.component.testing.Constants.KAFKA_BROKER_PORT;
import static com.flipkart.component.testing.Utils.deleteFolder;

/**
 * Local Kafka Server on PORT 5000
 */
class KafkaLocalServer extends DependencyInitializer {
    private KafkaServerStartable kafka;
    private static final String KAFKA_LOG_DIR_CONF = "log.dir";
    private static final int BROKER_ID = 0;


    KafkaLocalServer() {
    }

    public void initialize() {
        Properties kafkaProperties = getKafkaProperties();

        File kafkaLogDir = new File(kafkaProperties.getProperty(KAFKA_LOG_DIR_CONF));

        if (kafkaLogDir.exists()) {
            deleteFolder(kafkaLogDir);
        }

        KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);

        //start local zookeeper
        new ZookeeperLocalServer().initialize();

        //start local kafka broker
        kafka = new KafkaServerStartable(kafkaConfig);
        kafka.startup();
    }

    @Override
    public void shutDown() {
        kafka.shutdown();
    }


    private static Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.put("port", KAFKA_BROKER_PORT + "");
        properties.put("broker.id", BROKER_ID + "");
        properties.put("log.dir", KAFKA_LOG_DIR_CONF);
        properties.put("zookeeper.connect", ZookeeperLocalServer.ZOOKEEPER_HOST);
        properties.put("default.replication.factor", "1");
        properties.put("delete.topic.enable", "true");
        properties.put("auto.create.topics.enable", "true");
        properties.put("host.name", "127.0.0.1");
        return properties;
    }

}