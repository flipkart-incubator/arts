package com.flipkart.component.testing;


import com.flipkart.component.testing.model.TestConfig;
import com.flipkart.component.testing.model.kafka.KafkaIndirectInput;
import com.flipkart.component.testing.model.kafka.KafkaObservation;
import kafka.server.KafkaConfig;
import kafka.server.KafkaServerStartable;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 */
class KafkaLocalServer implements DependencyInitializer<KafkaIndirectInput, KafkaObservation, TestConfig> {
    private KafkaServerStartable kafka;
    private static final String KAFKA_LOG_DIR_CONF = "log.dir";
    private static final String KAFKA_DATA_DIR = "dataDir";
    private static final String LOGS_DIR = "logs";
    private static final int BROKER_ID = 0;
    private ZookeeperLocalServer zookeeperLocalServer = new ZookeeperLocalServer();


    private void deleteFolders() {
        List<File> folders = Arrays.asList(new File(KAFKA_LOG_DIR_CONF), new File(KAFKA_DATA_DIR), new File(LOGS_DIR));
        folders.stream().filter(File::exists).forEach(Utils::deleteFolder);
    }

    @Override
    public void initialize(TestConfig testConfig) throws Exception {
        Properties kafkaProperties = getKafkaProperties();

        deleteFolders();
        KafkaConfig kafkaConfig = new KafkaConfig(kafkaProperties);
        this.zookeeperLocalServer.initialize(null);

        //start local kafka broker
        kafka = new KafkaServerStartable(kafkaConfig);
        kafka.startup();
    }

    @Override
    public void shutDown() {
        kafka.shutdown();
    }

    @Override
    public void clean() {
        kafka.shutdown();
        this.zookeeperLocalServer.shutDown();
        try {
            this.initialize(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<KafkaIndirectInput> getIndirectInputClass() {
        return KafkaIndirectInput.class;
    }

    @Override
    public Class<KafkaObservation> getObservationClass() {
        return KafkaObservation.class;
    }


    private static Properties getKafkaProperties() {
        Properties properties = new Properties();
        properties.put("port", KafkaDataLoader.KAFKA_BROKER_PORT + "");
        properties.put("broker.id", BROKER_ID + "");
        properties.put("log.dir", KAFKA_LOG_DIR_CONF);
        properties.put("zookeeper.connect", "localhost:2181");
        properties.put("default.replication.factor", "1");
        properties.put("delete.topic.enable", "true");
        properties.put("auto.create.topics.enable", "true");
        properties.put("host.name", "127.0.0.1");
        return properties;
    }

}