package com.flipkart.component.testing.servers;


import com.flipkart.component.testing.internal.Utils;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.flipkart.component.testing.internal.Constants.KAFKA_BROKER_PORT;

/**
 */
class KafkaLocalServer implements DependencyInitializer {
    private KafkaServerStartable kafka;
    private static final String KAFKA_LOG_DIR_CONF = "log.dir";
    private static final String KAFKA_DATA_DIR = "dataDir";
    private static final String LOGS_DIR = "logs";
    private static final int BROKER_ID = 0;


    KafkaLocalServer() {
    }

    public void initialize() {
        Properties kafkaProperties = getKafkaProperties();

        deleteFolders();


        KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);

        //start local zookeeper
        new ZookeeperLocalServer().initialize();

        //start local kafka broker
        kafka = new KafkaServerStartable(kafkaConfig);
        kafka.startup();
    }

    private void deleteFolders() {
        List<File> folders = Arrays.asList(new File(KAFKA_LOG_DIR_CONF), new File(KAFKA_DATA_DIR), new File(LOGS_DIR));
        folders.stream().filter(File::exists).forEach(Utils::deleteFolder);
    }

    @Override
    public void shutDown() {
        kafka.shutdown();
    }

    @Override
    public void clean() {
        kafka.shutdown();
        this.initialize();
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