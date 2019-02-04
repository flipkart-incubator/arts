package com.flipkart.component.testing.shared;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.config.discovery.InstanceDiscoveryConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nio.serialization.Serializer;

import java.util.Map;

import static com.flipkart.component.testing.internal.Constants.HZ_INSTANCE_NAME;
import static com.flipkart.component.testing.internal.Constants.HZ_LOGGING_PROPERTY;
import static com.flipkart.component.testing.internal.Constants.ZOOKEEPER_PORT;

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
        config.setInstanceName(HZ_INSTANCE_NAME);
        config.setProperty(HZ_LOGGING_PROPERTY, "slf4j");
        config.getGroupConfig().setName(hazelcastTestConfig.getUser()).setPassword(
                hazelcastTestConfig.getPassword());

        InstanceDiscoveryConfig instanceDiscoveryConfig = new InstanceDiscoveryConfig();
        instanceDiscoveryConfig.setClusterId(hazelcastTestConfig.getGroup());
        instanceDiscoveryConfig.setEnabled(true); // enable instance discovery config
        instanceDiscoveryConfig.setZkConnectionString(String.format("localhost:%d", ZOOKEEPER_PORT));

        //Set instanceDiscoveryConfig in hz instance Config to enable discovery via local zookeeper
        config.getNetworkConfig().getJoin().setInstanceDiscoveryConfig(instanceDiscoveryConfig);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);

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
