package com.flipkart.component.testing.servers;


import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.io.File;
import java.io.IOException;

import static com.flipkart.component.testing.internal.Constants.ZOOKEEPER_PORT;
import static com.flipkart.component.testing.internal.Constants.ZKR_CONNECTIONS;
import static com.flipkart.component.testing.internal.Constants.ZKR_TICK_TIME;

class ZookeeperLocalServer implements DependencyInitializer {

    private static final String ZOOKEEPER_DIR_CONF = "dataDir";
    private static ZookeeperLocalServer instance;
    private ServerCnxnFactory standaloneServerFactory = null;
    static final String ZOOKEEPER_HOST = String.format("localhost:%d", ZOOKEEPER_PORT);
    private ZooKeeperServer server = null;
    private boolean initialized = false;

    private ZookeeperLocalServer() {
    }

    static ZookeeperLocalServer getInstance(){
        if(instance == null){
            instance = new ZookeeperLocalServer();
        }

        return instance;
    }

    @Override
    public void initialize() {
        if(this.initialized) return;
        this.initialized = true;

        File dir = new File(ZOOKEEPER_DIR_CONF, "zookeeper").getAbsoluteFile();

        try {
            server = new ZooKeeperServer(dir, dir, ZKR_TICK_TIME);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            standaloneServerFactory = ServerCnxnFactory.createFactory(ZOOKEEPER_PORT, ZKR_CONNECTIONS);
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
        this.initialize();
    }


}