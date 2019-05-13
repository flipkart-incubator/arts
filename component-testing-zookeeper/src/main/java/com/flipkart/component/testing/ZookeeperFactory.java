package com.flipkart.component.testing;

public class ZookeeperFactory {

    public static final int ZOOKEEPER_PORT = 2181;
    public static final int ZKR_TICK_TIME = 2000;
    public static final int ZKR_CONNECTIONS = 5;

    public static ZookeeperOperations getZookeeperOperations() {
        return ZookeeperOperationsImpl.getInstance();
    }
}
