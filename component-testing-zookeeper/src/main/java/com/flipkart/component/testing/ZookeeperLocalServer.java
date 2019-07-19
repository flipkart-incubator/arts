package com.flipkart.component.testing;


import com.flipkart.component.testing.model.TestConfig;
import com.flipkart.component.testing.model.zookeeper.ZookeeperIndirectInput;
import com.flipkart.component.testing.model.zookeeper.ZookeeperObservation;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.io.File;
import java.io.IOException;

public class ZookeeperLocalServer implements DependencyInitializer<ZookeeperIndirectInput, ZookeeperObservation, TestConfig> {

    private static final String ZOOKEEPER_DIR_CONF = "dataDir";
    private static ZookeeperLocalServer instance;
    private ServerCnxnFactory standaloneServerFactory = null;
    static final String ZOOKEEPER_HOST = String.format("localhost:%d", ZookeeperFactory.ZOOKEEPER_PORT);
    private ZooKeeperServer server = null;
    private boolean initialized = false;

    static ZookeeperLocalServer getInstance(){
        if(instance == null){
            instance = new ZookeeperLocalServer();
        }
        return instance;
    }

    @Override
    public void initialize(TestConfig testConfig) {
        if(this.initialized) return;
        this.initialized = true;

        File dir = new File(ZOOKEEPER_DIR_CONF, "zookeeper").getAbsoluteFile();

        try {
            server = new ZooKeeperServer(dir, dir, ZookeeperFactory.ZKR_TICK_TIME);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            standaloneServerFactory = ServerCnxnFactory.createFactory(ZookeeperFactory.ZOOKEEPER_PORT, ZookeeperFactory.ZKR_CONNECTIONS);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        try {
            standaloneServerFactory.startup(server);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public void shutDown() {
        standaloneServerFactory.shutdown();
        server.shutdown();
        initialized = false;
    }

    @Override
    public void clean() {
        this.shutDown();
        this.initialize(null);
    }

    @Override
    public Class<ZookeeperIndirectInput> getIndirectInputClass() {
        return  ZookeeperIndirectInput.class;
    }

    @Override
    public Class<ZookeeperObservation> getObservationClass() {
        return ZookeeperObservation.class;
    }


}