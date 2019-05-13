package com.flipkart.component.testing;

import com.flipkart.component.testing.model.zookeeper.ZookeeperIndirectInput;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ZookeeperDataLoader implements TestDataLoader<ZookeeperIndirectInput>{

    /**
     * loads the indirectinput into relevant components
     *
     * @param indirectInput
     */
    @Override
    public void load(ZookeeperIndirectInput indirectInput) {
        ZookeeperOperations zookeeperOperations = ZookeeperFactory.getZookeeperOperations();
        Optional.ofNullable(indirectInput.getPathsToCreate()).ifPresent(x -> x.forEach(zookeeperOperations::createPath));
        Optional.ofNullable(indirectInput.getDataToLoad()).ifPresent(x -> x.forEach(zookeeperOperations::putValue));
    }

    /**
     * returns the indirectInput class associated with this loader.
     */
    @Override
    public List<Class<ZookeeperIndirectInput>> getIndirectInputClasses() {
        return Arrays.asList(ZookeeperIndirectInput.class);
    }
}
