package com.flipkart.component.testing.shared;

import com.flipkart.component.testing.internal.Constants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

public class ZookeeperOperationsImpl implements ZookeeperOperations {

    private static ZookeeperOperations instance;
    private final CuratorFramework client;


    private ZookeeperOperationsImpl(){
        int sleepMsBetweenRetries = 10;
        int maxRetries = 1;
        RetryPolicy retryPolicy = new RetryNTimes(maxRetries, sleepMsBetweenRetries);
        client = CuratorFrameworkFactory.newClient("127.0.0.1:" + Constants.ZOOKEEPER_PORT, retryPolicy);
        client.start();
    }


    public static ZookeeperOperations getInstance() {
        if(instance == null){
            instance = new ZookeeperOperationsImpl();
        }
        return instance;
    }


    @Override
    public boolean exists(String path) {
        try{
            return client.checkExists().forPath(path) != null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public byte[] getValue(String path) {
        try {
            return client.getData().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createPath(String path) {
        try {
            if(exists(path)){
                client.delete().deletingChildrenIfNeeded().forPath(path);
            }
            client.create().creatingParentsIfNeeded().forPath(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putValue(String path, String value) {
        try{
            this.client.setData().forPath(path, value.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
