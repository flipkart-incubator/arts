package com.flipkart.component.testing;

import com.flipkart.component.testing.model.hazelcast.HazelcastTestConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HazelCastFactory {

    public static final String HZ_INSTANCE_NAME = "regression";
    public static final String HZ_MAPS_DS = "maps";
    public static final String HZ_LOGGING_PROPERTY = "hazelcast.logging.type";
    public static final String HZ_IMAP_CLASS = "com.hazelcast.map.impl.proxy.MapProxyImpl";


    public static HazelcastInstance getHazelcastInstance(HazelcastTestConfig hazelcastTestConfig){
        HazelcastInstance hazelcastInstance;
        if (hazelcastTestConfig.isServerMode() && Hazelcast.getAllHazelcastInstances().isEmpty())
            hazelcastInstance = new HazelcastInstanceInitializer(hazelcastTestConfig).getHazelcastInstance();
        else
            hazelcastInstance = Hazelcast.getAllHazelcastInstances().iterator().next();
        return hazelcastInstance;
    }
}
