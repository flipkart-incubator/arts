package com.flipkart.component.testing.servers;


import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static com.flipkart.component.testing.internal.Constants.ZOOKEEPER_PORT;
import static com.flipkart.component.testing.internal.Utils.deleteFolder;

/**
 * Local Zookeeper server on PORT 2000
 */
class ZookeeperLocalServer extends DependencyInitializer {

    private static final String ZOOKEEPER_DIR_CONF = "dataDir";
    static final String ZOOKEEPER_HOST = String.format("localhost:%d", ZOOKEEPER_PORT);

    ZookeeperLocalServer() {
    }

    @Override
    public void initialize() {
        Properties zkProperties = getZookeeperProperties();
        QuorumPeerConfig quorumConfiguration = new QuorumPeerConfig();
        try {
            quorumConfiguration.parseProperties(zkProperties);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }

        ZooKeeperServerMain zooKeeperServer = new ZooKeeperServerMain();
        final ServerConfig configuration = new ServerConfig();
        configuration.readFrom(quorumConfiguration);


        new Thread() {
            public void run() {
                try {
                    zooKeeperServer.runFromConfig(configuration);
                } catch (IOException e) {
                    System.out.println("ZooKeeper Failed");
                    e.printStackTrace(System.err);
                }
            }
        }.start();

        File zookeeperDir = new File(zkProperties.getProperty(ZOOKEEPER_DIR_CONF));

        if (zookeeperDir.exists()) {
            deleteFolder(zookeeperDir);
        }
    }

    @Override
    public void shutDown() {

    }

    private static Properties getZookeeperProperties() {

        Properties properties = new Properties();
        properties.put("clientPort", ZOOKEEPER_PORT + "");
        properties.put("dataDir", new File(ZOOKEEPER_DIR_CONF).getAbsolutePath());
        return properties;
    }

}