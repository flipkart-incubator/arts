package com.flipkart.component.testing.servers;

import com.flipkart.component.testing.shared.HazelcastTestConfig;
import com.flipkart.component.testing.shared.ObjectFactory;
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
public class HazelCastLocalServer implements DependencyInitializer {

    private HazelcastInstance hazelcastInstance;
    private ZookeeperLocalServer zookeeperLocalServer;
    private final HazelcastTestConfig hazelcastTestConfig;


    HazelCastLocalServer(HazelcastTestConfig hazelcastTestConfig){
        this.hazelcastTestConfig = hazelcastTestConfig;
    }

    @Override
    public void initialize() {
        zookeeperLocalServer = new ZookeeperLocalServer();
        zookeeperLocalServer.initialize();
        hazelcastInstance = ObjectFactory.getHazelcastInstance(this.hazelcastTestConfig);
    }

    @Override
    public void shutDown() {
        hazelcastInstance.shutdown();
    }

    @Override
    public void clean() {
        hazelcastInstance.getDistributedObjects().forEach(distributedObject -> {
            if(distributedObject.getClass().getName().equals("com.hazelcast.core.IMap"))
                hazelcastInstance.getMap(distributedObject.getName()).evictAll();
        });
    }
}
