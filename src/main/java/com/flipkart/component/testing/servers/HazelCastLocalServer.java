package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.model.hazelcast.HazelcastIndirectInput;
import com.flipkart.component.testing.shared.HazelcastTestConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.config.discovery.InstanceDiscoveryConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.nio.serialization.Serializer;

import java.util.Map;

import static com.flipkart.component.testing.internal.Constants.HZ_INSTANCE_NAME;
import static com.flipkart.component.testing.internal.Constants.ZOOKEEPER_PORT;

/**
 * @author siddharth.t
 */
public class HazelCastLocalServer implements DependencyInitializer {

    private HazelcastInstance hazelcastInstance;
    private ZookeeperLocalServer zookeeperLocalServer;
    private final HazelcastIndirectInput hazelcastIndirectInput;


    HazelCastLocalServer(HazelcastIndirectInput hazelcastIndirectInput){
        this.hazelcastIndirectInput = hazelcastIndirectInput;
    }

    @Override
    public void initialize() {
        zookeeperLocalServer = new ZookeeperLocalServer();
        zookeeperLocalServer.initialize();
        if (hazelcastIndirectInput.isServerMode()) {
            // if used in client server mode, we need create new instance
            Config config = buildHazelcastInstanceConfig(hazelcastIndirectInput);
            hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        } else {
        // or else will use the hazelcast instance initialized by client in its application code
        // even if there are multiple hazelcast running in different jvms clustered together, we will use
        // any one of them as all of them will be in sync through peer to peer communication
            if(hazelcastIndirectInput.isEmbeddedMode())
                hazelcastInstance = Hazelcast.getAllHazelcastInstances().iterator().next();
        }
    }

    @Override
    public void shutDown() {
        hazelcastInstance.shutdown();
        zookeeperLocalServer.shutDown();
    }

    @Override
    public void clean() {
        shutDown();
        initialize();
    }

    private Config buildHazelcastInstanceConfig(HazelcastTestConfig hazelcastTestConfig) {

        try {
            Config config = new Config();
            config.setInstanceName(HZ_INSTANCE_NAME);
            config.setProperty("hazelcast.logging.type", "slf4j");
            config.getGroupConfig().setName(hazelcastTestConfig.getGroupName()).setPassword(
                    hazelcastTestConfig.getPassword());

            InstanceDiscoveryConfig instanceDiscoveryConfig = new InstanceDiscoveryConfig();
            instanceDiscoveryConfig.setClusterId(hazelcastTestConfig.getGroupName());
            instanceDiscoveryConfig.setEnabled(true); // enable instance discovery config
            instanceDiscoveryConfig.setZkConnectionString(String.format("localhost:%d", ZOOKEEPER_PORT));

            //setting serialization config provided by user
            for (Map.Entry<String, String > serializerConfig : hazelcastTestConfig.getSerializerConfigMap().entrySet())
            {
                Class <?> serializerClazz = Class.forName(serializerConfig.getKey());
                Class <? extends Serializer> serializerImplClass = (Class<? extends Serializer>)
                        Class.forName(serializerConfig.getValue());
                Serializer serializer = serializerImplClass.newInstance();

                config.getSerializationConfig().getSerializerConfigs()
                        .add(new SerializerConfig().setTypeClass(serializerClazz)
                                .setImplementation(serializer));
            }
            //Set instanceDiscoveryConfig in hz instance Config to enable discovery via local zookeeper
            config.getNetworkConfig().getJoin().setInstanceDiscoveryConfig(instanceDiscoveryConfig);
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
            config.getNetworkConfig().setPortAutoIncrement(true);
            return config;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }

}
