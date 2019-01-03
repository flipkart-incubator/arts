package com.flipkart.component.testing.servers;


import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ZooKeeperServer;

import java.io.File;
import java.io.IOException;

import static com.flipkart.component.testing.internal.Constants.ZOOKEEPER_PORT;
import static com.flipkart.component.testing.internal.Constants.ZKR_CONNECTIONS;
import static com.flipkart.component.testing.internal.Constants.ZKR_TICK_TIME;


/**
 * Local Zookeeper server on PORT 2000
 */
class ZookeeperLocalServer implements DependencyInitializer {

    private static final String ZOOKEEPER_DIR_CONF = "dataDir";
    private ServerCnxnFactory standaloneServerFactory = null;
    static final String ZOOKEEPER_HOST = String.format("localhost:%d", ZOOKEEPER_PORT);
    private ZooKeeperServer server = null;

    ZookeeperLocalServer() {
    }

    @Override
    public void initialize() {
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
    }

    @Override
    public void clean() {
        this.shutDown();
        this.initialize();
    }


}