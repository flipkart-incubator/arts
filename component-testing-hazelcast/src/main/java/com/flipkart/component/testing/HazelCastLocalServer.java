package com.flipkart.component.testing;

import com.flipkart.component.testing.model.hazelcast.*;
import com.hazelcast.core.HazelcastInstance;

/**
 * @author siddharth.t
 */
public class HazelCastLocalServer implements DependencyInitializer<ServerHZIndirectInput, HazelcastObservation, HazelcastTestConfig> {

    private HazelcastInstance hazelcastInstance;
    private ZookeeperLocalServer zookeeperLocalServer =  ZookeeperLocalServer.getInstance();
    private HazelcastTestConfig hazelcastTestConfig;

    @Override
    public void initialize(HazelcastTestConfig testConfig) throws Exception {
        this.hazelcastTestConfig = testConfig;
        zookeeperLocalServer.initialize(null);
        hazelcastInstance = HazelCastFactory.getHazelcastInstance(this.hazelcastTestConfig);
    }

    @Override
    public void shutDown() {
        hazelcastInstance.shutdown();
    }

    @Override
    public void clean() {
        hazelcastInstance.getDistributedObjects().forEach(distributedObject -> {
            if(distributedObject.getClass().getName().equals(HazelCastFactory.HZ_IMAP_CLASS))
                hazelcastInstance.getMap(distributedObject.getName()).clear();
        });
    }

    @Override
    public Class<ServerHZIndirectInput> getIndirectInputClass() {
        return ServerHZIndirectInput.class;
    }

    @Override
    public Class<HazelcastObservation> getObservationClass() {
        return HazelcastObservation.class;
    }
}
