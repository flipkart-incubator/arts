package com.flipkart.component.testing;

import com.flipkart.component.testing.model.hazelcast.HazelcastTestConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nio.serialization.Serializer;
import com.hazelcast.spi.properties.GroupProperty;
import com.hazelcast.zookeeper.ZookeeperDiscoveryProperties;
import com.hazelcast.zookeeper.ZookeeperDiscoveryStrategyFactory;

import java.util.Map;

/**
 * @author siddharth.t
 */
public class HazelcastInstanceInitializer {

    private final HazelcastTestConfig hazelcastTestConfig;
    private HazelcastInstance hazelcastInstance;

    HazelcastInstanceInitializer(HazelcastTestConfig hazelcastTestConfig) {
        this.hazelcastTestConfig = hazelcastTestConfig;
        initialize();
    }

    HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    private void initialize() {
        Config config = buildHazelcastInstanceConfig(this.hazelcastTestConfig);
        hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    }

    private Config buildHazelcastInstanceConfig(HazelcastTestConfig hazelcastTestConfig) {

        Config config = new Config();
        config.setInstanceName(HazelCastFactory.HZ_INSTANCE_NAME);
        config.setProperty(HazelCastFactory.HZ_LOGGING_PROPERTY, "slf4j");
        config.getGroupConfig().setName(hazelcastTestConfig.getUser()).setPassword(
                hazelcastTestConfig.getPassword());

        //Set instanceDiscoveryConfig in hz instance Config to enable discovery via local zookeeper
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);

        config.setProperty("hazelcast.local.localAddress","127.0.0.1");
        config.setProperty(GroupProperty.DISCOVERY_SPI_ENABLED.getName(), "true");
        DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(new ZookeeperDiscoveryStrategyFactory());
        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_URL.key(), String.format("localhost:%d", 2181));
        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_PATH.key(), hazelcastTestConfig.getDiscoveryPath() );
        discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.GROUP.key(), hazelcastTestConfig.getGroup());
        config.getNetworkConfig().getJoin().getDiscoveryConfig().addDiscoveryStrategyConfig(discoveryStrategyConfig);

        //setting serialization config provided by user
        addSerializerConfig(config);
        return config;
    }

    private void addSerializerConfig(Config config) {

        for (Map.Entry<String, String> serializerConfig : hazelcastTestConfig.getSerializerConfigMap().entrySet()) {
            Class<?> serializerClazz;
            try {
                serializerClazz = Class.forName(serializerConfig.getKey());
                Class<? extends Serializer> serializerImplClass = (Class<? extends Serializer>)
                        Class.forName(serializerConfig.getValue());

                Serializer serializer;
                serializer = serializerImplClass.newInstance();
                config.getSerializationConfig().getSerializerConfigs()
                        .add(new SerializerConfig().setTypeClass(serializerClazz)
                                .setImplementation(serializer));
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
