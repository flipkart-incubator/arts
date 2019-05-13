package com.flipkart.component.testing;

public interface ZookeeperOperations {
    boolean exists(String path);

    byte[] getValue(String path);

    void createPath(String path);

    void putValue(String path, String value);
}
